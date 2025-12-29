package com.tlavu.educore.auth.authentication.application.service.interfaces;

import com.tlavu.educore.auth.authentication.domain.entity.RefreshToken;
import com.tlavu.educore.auth.authentication.domain.valueobject.RefreshTokenValue;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    RefreshToken issue(UUID userId);
    RefreshToken rotate(RefreshTokenValue refreshTokenValue);
    Optional<RefreshToken> validate(RefreshTokenValue refreshTokenValue);
    void revokeByUserId(UUID userId);
}
