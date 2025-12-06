package com.tlavu.educore.shared.infrastructure.config.app;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Loads environment variables from a .env file into the Spring application context.
 * <p>
 * This class implements {@link ApplicationContextInitializer} to ensure that environment variables
 * from the .env file are loaded into the Spring {@link ConfigurableEnvironment} before the application
 * context is fully initialized. This allows .env variables to be used in {@code @Value} annotations
 * and in {@code application.yaml} placeholders.
 * <p>
 * {@code ApplicationContextInitializer} is used instead of a {@code @Configuration} class with
 * {@code @PropertySource} because it runs earlier in the Spring lifecycle, ensuring that properties
 * are available for resolution during context initialization.
 */
public class EnvironmentConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfig.class);
    private static final String DOTENV_DIRECTORY = "./";
    private static final String DOTENV_FILENAME = ".env";

    @Override
    public void initialize(@Nonnull ConfigurableApplicationContext applicationContext) {

        Map<String, Object> envMap = new HashMap<>();
        Path dotenvPath = Path.of(DOTENV_DIRECTORY, DOTENV_FILENAME);
        boolean dotenvFileExists = Files.exists(dotenvPath);

        Dotenv dotenv = Dotenv.configure()
                .directory(DOTENV_DIRECTORY)
                .filename(DOTENV_FILENAME)
                .ignoreIfMissing()
                .ignoreIfMalformed()
                .load();

        dotenv.entries().forEach(
                entry -> envMap.put(
                        entry.getKey(),
                        entry.getValue()
                )
        );

        if (dotenvFileExists) {
            logger.info("Environment variables loaded from .env successfully!");
        } else {
            logger.debug(".env file not found. Environment variables may be sourced from system environment, application.yaml, or other configuration sources.");
        }

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        environment.getPropertySources()
                .addFirst(new MapPropertySource(
                        "dotenvProperties",
                        envMap
                ));

        boolean validate = Boolean.parseBoolean(
                environment.getProperty(
                        "app.env.validate-required",
                        "true"
                )
        );

        if (validate) {
            validateRequiredEnvVars(environment, dotenvFileExists);
        }
    }

    /**
     * Ensures required environment variables exist before application startup.
     *
     * @param environment the Spring environment containing all property sources
     * @param dotenvFileExists indicates whether the .env file exists on disk
     */
    private void validateRequiredEnvVars(ConfigurableEnvironment environment, boolean dotenvFileExists) {
        List<String> required = List.of(
                "DB_URL",
                "DB_USERNAME",
                "DB_PASSWORD"
        );

        logger.debug("Validating required environment variables ({} keys)...", required.size());

        List<String> missing = required.stream()
                .filter(key -> {
                    String value = environment.getProperty(key);
                    return value == null || value.isBlank();
                })
                .toList();

        if (!missing.isEmpty()) {
            boolean isDev = environment.matchesProfiles("dev");

            String errorMessage = buildErrorMessage(
                    missing,
                    dotenvFileExists,
                    isDev
            );

            logger.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        logger.debug("All required environment variables are present.");
    }

    /**
     * Builds an appropriate error message based on profile and whether the .env file exists.
     *
     * @param missing list of missing env variable names
     * @param dotenvFileExists does .env exist
     * @param isDevProfile whether the active profile is dev
     * @return formatted error message
     */
    private String buildErrorMessage(List<String> missing, boolean dotenvFileExists, boolean isDevProfile) {

        String joined = String.join(", ", missing);

        if (isDevProfile) {
            String hint = dotenvFileExists
                    ? "Please ensure they are defined in your .env file."
                    : ".env file was not found. Please define them in system environment or application.yaml.";

            return String.format(
                    "Missing required environment variables: %s. %s",
                    joined,
                    hint
            );
        }

        return "Missing required environment variables. Check your deployment configuration.";
    }
}