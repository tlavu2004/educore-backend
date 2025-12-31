package com.tlavu.educore.auth.authentication.application.dto.response;

import com.tlavu.educore.auth.user.application.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResult {

    private final String accessToken;
    private final String refreshToken;
    private final Long expiresIn;
}
