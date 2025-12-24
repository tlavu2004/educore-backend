package com.tlavu.educore.auth.user.domain.entity;

import com.tlavu.educore.auth.shared.domain.entity.BaseDomainEntity;
import com.tlavu.educore.auth.user.domain.exception.ActivationTokenAlreadyUsedException;
import com.tlavu.educore.auth.user.domain.exception.ActivationTokenExpiredException;
import com.tlavu.educore.auth.user.domain.valueobject.ActivationTokenValue;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class ActivationToken extends BaseDomainEntity<UUID> {

    private final ActivationTokenValue tokenValue;
    private final UUID userId;
    private final Instant expiresAt;
    private Instant usedAt;

    public ActivationToken(
            UUID id,
            ActivationTokenValue tokenValue,
            UUID userId,
            Instant expiresAt
    ) {
        this.id = id;
        this.tokenValue = tokenValue;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public static ActivationToken createNew(
            ActivationTokenValue tokenValue,
            UUID userId,
            Instant expiresAt
    ) {
        Objects.requireNonNull(tokenValue, "tokenValue cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");

        Instant now = Instant.now();
        return new ActivationToken(
                UUID.randomUUID(),
                tokenValue,
                userId,
                expiresAt
        );
    }

    public static ActivationToken reconstruct(
            UUID id,
            ActivationTokenValue tokenValue,
            UUID userId,
            Instant expiresAt,
            Instant usedAt
    ) {
        ActivationToken activationToken = new ActivationToken(
                id,
                tokenValue,
                userId,
                expiresAt
        );
        activationToken.usedAt = usedAt;
        return activationToken;
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isValid(Instant now) {
        return !isUsed() && !isExpired(now);
    }

    public boolean canBeUsedAt(Instant now) {
        return !isUsed() && !isExpired(now);
    }

    public void markAsUsed(Instant now) {
        if (isUsed()) {
            throw new ActivationTokenAlreadyUsedException("Activation token has already been used.");
        }
        if (isExpired(now)) {
            throw new ActivationTokenExpiredException("Activation token has expired.");
        }
        this.usedAt = Instant.now();
    }
}

