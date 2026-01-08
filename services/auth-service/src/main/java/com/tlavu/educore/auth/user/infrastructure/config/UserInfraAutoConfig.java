package com.tlavu.educore.auth.user.infrastructure.config;

import com.tlavu.educore.auth.authentication.domain.port.PasswordHasher;
import com.tlavu.educore.auth.authentication.infrastructure.security.SpringPasswordHasherAdapter;
import com.tlavu.educore.auth.user.domain.repository.ActivationTokenRepository;
import com.tlavu.educore.auth.user.infrastructure.persistence.InMemoryActivationTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserInfraAutoConfig {

    @Bean
    public ActivationTokenRepository activationTokenRepository() {
        return new InMemoryActivationTokenRepository();
    }

    @Bean
    public PasswordHasher passwordHasher(PasswordEncoder passwordEncoder) {
        return new SpringPasswordHasherAdapter(passwordEncoder);
    }
}

