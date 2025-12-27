package com.tlavu.educore.auth.user.application.usecase.command;

import java.util.UUID;

public record ActivateUserCommand(
        UUID userId
) {}
