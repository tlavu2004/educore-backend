package com.tlavu.educore.auth.authentication.application.usecase.command;

public record RefreshTokenCommand (
        String refreshToken
) {}
