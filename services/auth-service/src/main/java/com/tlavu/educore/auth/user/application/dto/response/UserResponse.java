package com.tlavu.educore.auth.user.application.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String fullName,
        String role,
        String status,
        Instant lastLoginAt,
        Instant createdAt
) {}
