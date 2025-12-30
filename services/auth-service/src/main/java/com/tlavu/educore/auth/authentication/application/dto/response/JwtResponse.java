package com.tlavu.educore.auth.authentication.application.dto.response;

public record JwtResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn  // Expiration time in seconds
) {
    public static JwtResponse from(String accessToken, String refreshToken, Long expiresIn) {
        return new JwtResponse(
                accessToken,
                refreshToken,
                "Bearer",
                expiresIn
        );
    }
}