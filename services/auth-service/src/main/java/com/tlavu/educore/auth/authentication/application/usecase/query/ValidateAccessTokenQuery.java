package com.tlavu.educore.auth.authentication.application.usecase.query;

public record ValidateAccessTokenQuery(
    String accessToken
) {

    public ValidateAccessTokenQuery {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("Access token cannot be null or blank");
        }
    }
}
