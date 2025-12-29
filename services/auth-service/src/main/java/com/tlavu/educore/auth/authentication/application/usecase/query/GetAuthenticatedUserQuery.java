package com.tlavu.educore.auth.authentication.application.usecase.query;

public record GetAuthenticatedUserQuery (
    String accessToken
) {

    public GetAuthenticatedUserQuery {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("Access token cannot be null or blank");
        }
    }
}
