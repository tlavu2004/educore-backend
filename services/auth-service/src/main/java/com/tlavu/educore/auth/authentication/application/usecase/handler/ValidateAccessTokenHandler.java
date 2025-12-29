package com.tlavu.educore.auth.authentication.application.usecase.handler;

import com.tlavu.educore.auth.authentication.application.service.interfaces.AccessTokenService;
import com.tlavu.educore.auth.authentication.application.usecase.query.ValidateAccessTokenQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidateAccessTokenHandler {

    private final AccessTokenService accessTokenService;

    public boolean handle(ValidateAccessTokenQuery query) {
        return accessTokenService.validate(query.accessToken());
    }
}
