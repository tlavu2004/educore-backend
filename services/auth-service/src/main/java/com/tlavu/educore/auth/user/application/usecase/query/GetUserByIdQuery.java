package com.tlavu.educore.auth.user.application.usecase.query;

import java.util.UUID;

public record GetUserByIdQuery(
        UUID userId
) {

    public GetUserByIdQuery {
        if (userId == null || userId.toString().isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
