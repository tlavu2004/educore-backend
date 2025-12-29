package com.tlavu.educore.auth.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponse {

    private final UUID id;
    private final String email;
    private final String fullName;
    private final String role;
    private final String status;
    private final Instant lastLoginAt;
}
