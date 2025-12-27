package com.tlavu.educore.auth.user.application.usecase.command;

public record CreateUserCommand(
        String email,
        String rawPassword,
        String fullName
) {}