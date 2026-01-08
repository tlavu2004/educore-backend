package com.tlavu.educore.auth.authentication.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResult {

    private final String accessToken;
    private final String refreshToken;
    private final Long expiresIn;
}
