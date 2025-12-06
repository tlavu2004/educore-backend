package com.tlavu.educore.shared.infrastructure.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for bcrypt password hashing strength.
 * <p>
 * Binds the property <code>security.bcrypt.strength</code> from the application configuration.
 * The <code>strength</code> parameter determines the computational cost of bcrypt hashing.
 * <p>
 * <b>Default value:</b> 10
 * <br>
 * <b>Security implications:</b> Higher strength values increase the time required to hash and verify passwords,
 * improving resistance to brute-force attacks, but may impact application performance. The default value (10)
 * is a reasonable balance for most applications, but should be increased if security requirements permit.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.bcrypt")
public class BcryptConfig {
    private int strength = 10;
}
