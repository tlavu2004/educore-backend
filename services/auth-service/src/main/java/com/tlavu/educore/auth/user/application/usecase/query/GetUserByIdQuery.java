package com.tlavu.educore.auth.user.application.usecase.query;

import java.util.UUID;

public record GetUserByIdQuery(
        UUID userId
) {}
