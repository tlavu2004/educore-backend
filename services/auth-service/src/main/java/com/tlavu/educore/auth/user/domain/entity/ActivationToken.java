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

    private final ActivationTokenValue activationTokenValue;
    private final UUID userId;
    private final Instant expiresAt;
    private Instant usedAt;

    public ActivationToken(
            UUID id,
            ActivationTokenValue activationTokenValue,
            UUID userId,
            Instant expiresAt
    ) {
        this.id = id;
        this.activationTokenValue = activationTokenValue;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.markCreated();
    }

    public static ActivationToken createNew(
            ActivationTokenValue activationTokenValue,
            UUID userId,
            Instant expiresAt
    ) {
        Objects.requireNonNull(activationTokenValue, "activationTokenValue cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");

        if (expiresAt.isBefore(Instant.now())) {
            throw new ActivationTokenExpiredException("expiresAt must be in the future");
        }

        return new ActivationToken(
                UUID.randomUUID(),
                activationTokenValue,
                userId,
                expiresAt
        );
    }

    public static ActivationToken reconstruct(
            UUID id,
            ActivationTokenValue activationTokenValue,
            UUID userId,
            Instant expiresAt,
            Instant usedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        ActivationToken activationToken = new ActivationToken(
                id,
                activationTokenValue,
                userId,
                expiresAt
        );
        activationToken.usedAt = usedAt;
        activationToken.createdAt = createdAt;
        activationToken.updatedAt = updatedAt;
        return activationToken;
    }

    public boolean isExpired(Instant now) {
        Objects.requireNonNull(now, "now cannot be null");
        return now.isAfter(expiresAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isValid(Instant now) {
        Objects.requireNonNull(now, "now cannot be null");
        return !isUsed() && !isExpired(now);
    }

    public void markAsUsed(Instant now) {
        Objects.requireNonNull(now, "now cannot be null");

        if (isUsed()) {
            throw new ActivationTokenAlreadyUsedException("Activation token has already been used.");
        }
        if (isExpired(now)) {
            throw new ActivationTokenExpiredException("Activation token has expired.");
        }

        this.usedAt = now;
        this.markUpdated();
    }
}
