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
import java.util.Map;

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

            logger.info("Environment variables loaded from .env successfully!");
        } catch (Exception e) {
            logger.warn("Warning: Could not load .env file. Using defaults from application.yaml");
        }
    }
}