package com.tlavu.educore.auth.user.presentation.dto.response;

import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.enums.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String email,
        String fullName,
        UserRole role,
        UserStatus status,
        Instant createdAt,
        Instant lastLoginAt
) {}
