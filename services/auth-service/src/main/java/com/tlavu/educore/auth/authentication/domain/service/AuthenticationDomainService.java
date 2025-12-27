package com.tlavu.educore.auth.authentication.domain.service;

import com.tlavu.educore.auth.authentication.domain.entity.RefreshToken;
import com.tlavu.educore.auth.authentication.domain.exception.RefreshTokenExpiredException;
import com.tlavu.educore.auth.authentication.domain.exception.RefreshTokenRevokedException;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.service.UserDomainService;

import java.time.Instant;

public class AuthenticationDomainService {

    private final UserDomainService userDomainService;

    public AuthenticationDomainService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    public void ensureUserCanAuthenticate(User user) {
        userDomainService.ensureUserCanLogin(user);
    }

    public void ensureRefreshTokenIsValid(RefreshToken refreshToken, Instant now) {
        if (refreshToken.isRevoked()) {
            throw new RefreshTokenRevokedException("Refresh token has been revoked");
        }

        if (refreshToken.isExpired(now)) {
            throw new RefreshTokenExpiredException("Refresh token has expired");
        }
    }
}
