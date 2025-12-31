package com.tlavu.educore.auth.authentication.application.usecase.handler;

import com.tlavu.educore.auth.authentication.application.service.interfaces.RefreshTokenService;
import com.tlavu.educore.auth.authentication.application.usecase.command.LogoutCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LogoutHandler {

    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void handle(LogoutCommand command) {
        refreshTokenService.revokeByUserId(command.userId());
    }
}
