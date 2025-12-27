package com.tlavu.educore.auth.user.domain.repository;

import com.tlavu.educore.auth.user.domain.entity.ActivationToken;
import com.tlavu.educore.auth.user.domain.valueobject.ActivationTokenValue;

import java.util.Optional;
import java.util.UUID;

public interface ActivationTokenRepository {

    ActivationToken save(ActivationToken token);
    Optional<ActivationToken> findByToken(ActivationTokenValue token);
    Optional<ActivationToken> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
