package com.tlavu.educore.shared.infrastructure.config.app;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

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

    @Override
    public void initialize(@Nonnull ConfigurableApplicationContext applicationContext) {

        Map<String, Object> envMap = new HashMap<>();
        boolean dotenvFileLoaded = false;

        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .filename(".env")
                    .ignoreIfMissing()
                    .ignoreIfMalformed()
                    .load();

            dotenv.entries().forEach(
                    entry -> envMap.put(
                            entry.getKey(),
                            entry.getValue()
                    )
            );

            // Check if .env file was actually loaded (non-empty entries indicate file was present)
            dotenvFileLoaded = !envMap.isEmpty();
            
            if (dotenvFileLoaded) {
                logger.info("Environment variables loaded from .env successfully!");
            } else {
                logger.debug(".env file not found or is empty. Environment variables may be sourced from system environment, application.yaml, or other configuration sources.");
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
            validateRequiredEnvVars(environment, dotenvFileLoaded);
        }
    }

    /**
     * Ensures required environment variables exist before application startup.
     *
     * @param environment the Spring environment containing all property sources
     * @param dotenvFileLoaded indicates whether the .env file was successfully loaded
     */
    private void validateRequiredEnvVars(ConfigurableEnvironment environment, boolean dotenvFileLoaded) {
        List<String> required = List.of(
                "DB_URL",
                "DB_USERNAME",
                "DB_PASSWORD"
        );

        // Log which variables are present in the .env file for debugging
        MapPropertySource dotenvSource = (MapPropertySource) environment
                .getPropertySources()
                .get("dotenvProperties");

        if (dotenvFileLoaded && dotenvSource != null) {
            logger.debug("Variables in .env file: {}", dotenvSource.getSource().keySet());
        } else if (!dotenvFileLoaded) {
            logger.debug(".env file was not loaded; validating against all property sources (system environment, application.yaml, etc.)");
        } else {
            logger.debug("dotenvProperties source not found; proceeding to check all property sources.");
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
            String errorMessage = dotenvFileLoaded
                    ? "Missing required environment variables: " + missing + ". Please ensure they are defined in .env file or system environment."
                    : "Missing required environment variables: " + missing + ". .env file was not found. Please ensure these variables are defined in system environment or application.yaml.";
            
            logger.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        logger.info("All required environment variables are present.");
    }
}