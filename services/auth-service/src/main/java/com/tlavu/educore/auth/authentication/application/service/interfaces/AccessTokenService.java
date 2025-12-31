package com.tlavu.educore.auth.authentication.application.service.interfaces;

import com.tlavu.educore.auth.user.domain.entity.User;

import java.time.Instant;
import java.util.UUID;

public interface AccessTokenService {

    String generate(UUID userId, String email, String role);
    boolean validate(String token);
    UUID extractUserId(String token);
    Instant extractExpiration(String token);
}
