package com.tlavu.educore.auth.user.application.usecase.query;

public record GetUserByEmailQuery(
        String email
) {

    public GetUserByEmailQuery {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
}
