package com.tlavu.educore.auth.authentication.domain.entity;

import com.tlavu.educore.auth.authentication.domain.exception.RefreshTokenExpiredException;
import com.tlavu.educore.auth.authentication.domain.valueobject.RefreshTokenValue;
import com.tlavu.educore.auth.shared.domain.entity.BaseDomainEntity;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class RefreshToken extends BaseDomainEntity<UUID> {

    private final RefreshTokenValue refreshTokenValue;
    private final UUID userId;
    private final Instant expiresAt;
    private boolean revoked;
    
    public RefreshToken(
            UUID id,
            RefreshTokenValue refreshTokenValue,
            UUID userId,
            Instant expiresAt
    ) {
        this.id = id;
        this.refreshTokenValue = refreshTokenValue;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.markCreated();
    }
    
    public static RefreshToken createNew(
            RefreshTokenValue refreshTokenValue,
            UUID userId,
            Instant expiresAt
    ) {
        Objects.requireNonNull(refreshTokenValue, "refreshTokenValue cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");

        if (expiresAt.isBefore(Instant.now())) {
            throw new RefreshTokenExpiredException("expiresAt must be in the future");
        }

        return new RefreshToken(
                UUID.randomUUID(),
                refreshTokenValue,
                userId,
                expiresAt
        );
    }

    public static RefreshToken reconstruct(
            UUID id,
            RefreshTokenValue refreshTokenValue,
            UUID userId,
            Instant expiresAt,
            boolean revoked,
            Instant createAt,
            Instant updateAt
    ) {
        RefreshToken refreshToken = new RefreshToken(
                id,
                refreshTokenValue,
                userId,
                expiresAt
        );
        refreshToken.revoked = revoked;
        refreshToken.createdAt = createAt;
        refreshToken.updatedAt = updateAt;
        return refreshToken;
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }

    public boolean isValid(Instant now) {
        return !isExpired(now) && !revoked;
    }

    public void revoke() {
        if (!this.revoked) {
            this.revoked = true;
            markUpdated();
        }
    }
}

