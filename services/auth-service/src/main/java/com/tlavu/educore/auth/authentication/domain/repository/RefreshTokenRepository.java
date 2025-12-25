package com.tlavu.educore.auth.authentication.domain.repository;

import com.tlavu.educore.auth.authentication.domain.entity.RefreshToken;
import com.tlavu.educore.auth.authentication.domain.valueobject.RefreshTokenValue;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findByToken(RefreshTokenValue token);
    Optional<RefreshToken> findByUserId(UUID userId);
    void deleteByToken(RefreshTokenValue token);
    void deleteByUserId(UUID userId);
}
