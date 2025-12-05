package com.tlavu.educore.shared.infrastructure.config.app;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            Map<String, Object> envMap = new HashMap<>();
            dotenv.entries().forEach(
                    entry -> envMap.put(
                            entry.getKey(),
                            entry.getValue()
                    )
            );

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
                validateRequiredEnvVars(environment);
            }

            logger.info("Environment variables loaded from .env successfully!");
        } catch (Exception e) {
            logger.warn("Warning: Could not load .env file. Using defaults from application.yaml", e);
        }
    }

    /**
     * Ensures required environment variables exist before application startup.
     */
    private void validateRequiredEnvVars(ConfigurableEnvironment environment) {
        List<String> required = List.of(
                "DB_URL",
                "DB_USERNAME",
                "DB_PASSWORD"
        );

        List<String> missing = required.stream()
                .filter(key -> environment.getProperty(key) == null)
                .toList();

        if (!missing.isEmpty()) {
            throw new IllegalStateException(
                    "Missing required environment variables: " +
                    missing +
                    ". Please define them in .env or real environment."
            );
        }
    }
}