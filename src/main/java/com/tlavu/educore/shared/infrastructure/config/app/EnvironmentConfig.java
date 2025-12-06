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
import java.nio.file.Paths;
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
        boolean dotenvFileExists = false;

        try {
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

            // Check if .env file exists after successful loading
            // If the file existed (even if empty), Dotenv will load it successfully
            dotenvFileExists = Files.exists(Paths.get(DOTENV_DIRECTORY).resolve(DOTENV_FILENAME));
            
            if (dotenvFileExists) {
                logger.info("Environment variables loaded from .env successfully!");
            } else {
                logger.debug(".env file not found. Environment variables may be sourced from system environment, application.yaml, or other configuration sources.");
            }
        } catch (Exception e) {
            logger.debug("Could not load .env file. Environment variables may be sourced from system environment, application.yaml, or other configuration sources.", e);
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

        // Log which variables are present in the .env file for debugging
        MapPropertySource dotenvSource = (MapPropertySource) environment
                .getPropertySources()
                .get("dotenvProperties");

        if (dotenvSource != null && dotenvSource.getSource() != null && !dotenvSource.getSource().isEmpty()) {
            logger.debug("Variables in .env file: {}", dotenvSource.getSource().keySet());
        } else if (!dotenvFileExists) {
            logger.debug(".env file was not found; validating against all property sources (system environment, application.yaml, etc.)");
        } else {
            logger.debug("No variables loaded from .env; validating against all property sources.");
        }

        List<String> missing = required.stream()
                .filter(key -> {
                    String value = environment.getProperty(key);
                    boolean isMissing = value == null || value.isBlank();

                    logger.debug(
                            "Checking {} in all property sources: {} (missing: {})",
                            key,
                            value != null ? "EXISTS" : "NULL",
                            isMissing
                    );
                    return isMissing;
                })
                .toList();

        if (!missing.isEmpty()) {
            String errorMessage = buildErrorMessage(missing, dotenvFileExists);
            logger.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        logger.info("All required environment variables are present.");
    }

    /**
     * Builds an appropriate error message based on whether the .env file exists.
     */
    private String buildErrorMessage(List<String> missing, boolean dotenvFileExists) {
        if (dotenvFileExists) {
            return String.format(
                    "Missing required environment variables: %s. Please ensure they are defined in .env file or system environment.",
                    missing
            );
        } else {
            return String.format(
                    "Missing required environment variables: %s. .env file was not found. Please ensure these variables are defined in system environment or application.yaml.",
                    missing
            );
        }
    }
}