package com.tlavu.educore.auth.user.domain.entity;

import com.tlavu.educore.auth.shared.domain.entity.BaseDomainEntity;
import com.tlavu.educore.auth.user.domain.exception.ActivationTokenAlreadyUsedException;
import com.tlavu.educore.auth.user.domain.exception.ActivationTokenExpiredException;
import com.tlavu.educore.auth.user.domain.valueobject.ActivationTokenValue;
import com.tlavu.educore.auth.user.domain.valueobject.UserId;
import lombok.Getter;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class ActivationToken extends BaseDomainEntity<UUID> {

    private ActivationTokenValue activationTokenValue;
    private UserId userId;
    private Instant expiresAt;
    private Instant usedAt;

    private ActivationToken() {
        // For reconstruction purposes
    }

    protected ActivationToken(
            UUID id,
            ActivationTokenValue activationTokenValue,
            UserId userId,
            Instant expiresAt,
            Instant now
    ) {
        this.id = id;
        this.activationTokenValue = activationTokenValue;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.markCreated(now);
    }

    public static ActivationToken createNew(
            ActivationTokenValue activationTokenValue,
            UserId userId,
            Instant expiresAt,
            Clock clock
    ) {
        Objects.requireNonNull(activationTokenValue, "activationTokenValue cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");
        Objects.requireNonNull(clock, "clock cannot be null");

        Instant now = Instant.now(clock);
        if (expiresAt.isBefore(now)) {
            throw new ActivationTokenExpiredException("expiresAt must be in the future");
        }

        return new ActivationToken(
                UUID.randomUUID(),
                activationTokenValue,
                userId,
                expiresAt,
                now
        );
    }

    public static ActivationToken reconstruct(
            UUID id,
            ActivationTokenValue activationTokenValue,
            UserId userId,
            Instant expiresAt,
            Instant usedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(activationTokenValue, "activationTokenValue cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");
        Objects.requireNonNull(createdAt, "createdAt cannot be null");
        Objects.requireNonNull(updatedAt, "updatedAt cannot be null");

        ActivationToken token = new ActivationToken();

        token.id = id;
        token.activationTokenValue = activationTokenValue;
        token.userId = userId;
        token.expiresAt = expiresAt;
        token.usedAt = usedAt;
        token.createdAt = createdAt;
        token.updatedAt = updatedAt;

        return token;
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
        this.markUpdated(now);
    }
}
