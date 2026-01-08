 package com.tlavu.educore.auth.shared.infrastructure.security;

import com.tlavu.educore.auth.shared.application.security.AuthContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    @Bean
    public AuthContext authContext() {
        return new SpringSecurityAuthContext();
    }
}

