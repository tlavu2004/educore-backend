package com.tlavu.educore.auth.authentication.application.service.impl;

import com.tlavu.educore.auth.authentication.application.service.interfaces.RefreshTokenService;
import com.tlavu.educore.auth.authentication.domain.entity.RefreshToken;
import com.tlavu.educore.auth.authentication.domain.exception.InvalidRefreshTokenException;
import com.tlavu.educore.auth.authentication.domain.repository.RefreshTokenRepository;
import com.tlavu.educore.auth.authentication.domain.valueobject.RefreshTokenValue;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(30);
    private final RefreshTokenRepository refreshTokenRepository;
    private final Clock clock;

    @Override
    @Transactional
    public RefreshToken issue(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);

        // TODO: Set expiration from configuration (environment variable)
        RefreshToken refreshToken = RefreshToken.createNew(
                RefreshTokenValue.generate(),
                userId,
                Instant.now(clock).plus(REFRESH_TOKEN_TTL),
                clock
        );

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public RefreshToken rotate(RefreshTokenValue refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found"));

        refreshToken.revoke();

        RefreshToken newRefreshToken = RefreshToken.createNew(
                RefreshTokenValue.generate(),
                refreshToken.getUserId(),
                Instant.now(clock).plus(REFRESH_TOKEN_TTL),
                clock
        );

        refreshTokenRepository.save(refreshToken);
        return refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public Optional<RefreshToken> validate(RefreshTokenValue refreshTokenValue) {
        return refreshTokenRepository.findByToken(refreshTokenValue)
                .filter(token -> token.isValid(Instant.now(clock)));
    }

    @Override
    @Transactional
    public void revokeByUserId(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}

