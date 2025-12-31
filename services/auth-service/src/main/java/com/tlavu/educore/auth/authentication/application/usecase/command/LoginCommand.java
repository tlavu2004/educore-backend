package com.tlavu.educore.auth.authentication.application.usecase.command;

public record LoginCommand (
        String email,
        String password
) {}
