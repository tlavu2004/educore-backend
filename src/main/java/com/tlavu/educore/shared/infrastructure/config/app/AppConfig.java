package com.tlavu.educore.shared.infrastructure.config.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tlavu.educore.shared.infrastructure.config.security.BcryptConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application-wide configuration class.
 * <p>
 * Defines critical beans that affect security and serialization across the entire application:
 * <ul>
 *   <li><b>PasswordEncoder</b>: Uses BCrypt with a configurable strength (see {@link BcryptConfig}) to allow tuning of password hashing complexity for security and performance.</li>
 *   <li><b>ObjectMapper</b>: Configured to serialize Java 8 date/time types as ISO-8601 strings (not timestamps) for better interoperability and readability, and to ignore unknown properties during deserialization for robustness.</li>
 * </ul>
 * <p>
 * These decisions are made to ensure secure password storage and consistent, human-readable date/time serialization throughout the application.
 */
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder(BcryptConfig bcryptConfig) {
        return new BCryptPasswordEncoder(bcryptConfig.getStrength());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}