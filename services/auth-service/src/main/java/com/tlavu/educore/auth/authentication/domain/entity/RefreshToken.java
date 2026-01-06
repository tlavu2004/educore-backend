package com.tlavu.educore.auth.authentication.domain.entity;

import com.tlavu.educore.auth.authentication.domain.exception.InvalidRefreshTokenExpiryException;
import com.tlavu.educore.auth.authentication.domain.valueobject.RefreshTokenValue;
import com.tlavu.educore.auth.shared.domain.entity.BaseDomainEntity;
import com.tlavu.educore.auth.user.domain.valueobject.UserId;
import lombok.Getter;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class RefreshToken extends BaseDomainEntity<UUID> {

    private RefreshTokenValue refreshTokenValue;
    private UserId userId;
    private Instant expiresAt;
    private Instant revokedAt;

    private RefreshToken() {
        // For reconstruction purposes
    }

    protected RefreshToken(
            UUID id,
            RefreshTokenValue refreshTokenValue,
            UserId userId,
            Instant expiresAt,
            Instant now
    ) {
        this.id = id;
        this.refreshTokenValue = refreshTokenValue;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.markCreated(now);
    }
    
    public static RefreshToken createNew(
            RefreshTokenValue refreshTokenValue,
            UserId userId,
            Instant expiresAt,
            Clock clock
    ) {
        Objects.requireNonNull(refreshTokenValue, "refreshTokenValue cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");
        Objects.requireNonNull(clock, "clock cannot be null");

        Instant now = Instant.now(clock);
        if (expiresAt.isBefore(now)) {
            throw new InvalidRefreshTokenExpiryException("expiresAt must be in the future");
        }

        return new RefreshToken(
                UUID.randomUUID(),
                refreshTokenValue,
                userId,
                expiresAt,
                now
        );
    }

    public static RefreshToken reconstruct(
            UUID id,
            RefreshTokenValue refreshTokenValue,
            UserId userId,
            Instant expiresAt,
            Instant revokedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(refreshTokenValue, "refreshTokenValue cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");
        Objects.requireNonNull(createdAt, "createdAt cannot be null");
        Objects.requireNonNull(updatedAt, "updatedAt cannot be null");

        RefreshToken token = new RefreshToken();

        token.id = id;
        token.refreshTokenValue = refreshTokenValue;
        token.userId = userId;
        token.expiresAt = expiresAt;
        token.revokedAt = revokedAt;
        token.createdAt = createdAt;
        token.updatedAt = updatedAt;

        return token;
    }

    public boolean isExpired(Instant now) {
        Objects.requireNonNull(now, "now cannot be null");
        return now.isAfter(expiresAt);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isValid(Instant now) {
        Objects.requireNonNull(now, "now cannot be null");
        return !isExpired(now) && !isRevoked();
    }

    public void revoke(Instant now) {
        Objects.requireNonNull(now, "now cannot be null");

        if (this.revokedAt == null) {
            this.revokedAt = now;
            markUpdated(now);
        }
    }
}
