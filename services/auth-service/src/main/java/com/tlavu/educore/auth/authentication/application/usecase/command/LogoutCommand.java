package com.tlavu.educore.auth.authentication.application.usecase.command;

import java.util.UUID;

public record LogoutCommand (
        UUID userId
) {}
