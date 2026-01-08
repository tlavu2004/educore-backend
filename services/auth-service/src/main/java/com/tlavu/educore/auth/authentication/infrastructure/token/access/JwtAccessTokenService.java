package com.tlavu.educore.auth.authentication.infrastructure.token.access;

import com.tlavu.educore.auth.authentication.application.service.interfaces.AccessTokenService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtAccessTokenService implements AccessTokenService {

    private final JwtAccessTokenProvider provider;

    public JwtAccessTokenService(JwtAccessTokenProvider provider) {
        this.provider = provider;
    }

    @Override
    public String generate(UUID userId, String email, String role) {
        return provider.createToken(userId, email, role);
    }

    @Override
    public boolean validate(String token) {
        return provider.validate(token);
    }

    @Override
    public UUID extractUserId(String token) {
        return provider.extractUserId(token);
    }

    @Override
    public Instant extractExpiration(String token) {
        return provider.extractExpiration(token);
    }
}

