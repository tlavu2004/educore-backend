package com.tlavu.educore.auth.authentication.infrastructure.adapter.persistence;

import com.tlavu.educore.auth.authentication.domain.entity.RefreshToken;
import com.tlavu.educore.auth.authentication.domain.repository.RefreshTokenRepository;
import com.tlavu.educore.auth.authentication.domain.valueobject.RefreshTokenValue;
import com.tlavu.educore.auth.authentication.infrastructure.token.refresh.persistence.jpa.mapper.RefreshTokenJpaMapper;
import com.tlavu.educore.auth.authentication.infrastructure.token.refresh.persistence.jpa.repository.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class RefreshTokenPersistenceAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;
    private final RefreshTokenJpaMapper mapper;

    @Override
    @Transactional
    public RefreshToken save(RefreshToken token) {
        var entity = mapper.toEntity(token);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(RefreshTokenValue token) {
        return mapper.toDomain(jpaRepository.findByToken(token.toString()));
    }

    @Override
    public Optional<RefreshToken> findByUserId(UUID userId) {
        return mapper.toDomain(jpaRepository.findByUserId(userId));
    }

    @Override
    @Transactional
    public void deleteByToken(RefreshTokenValue token) {
        jpaRepository.deleteByToken(token.toString());
    }

    @Override
    @Transactional
    public void deleteByUserId(UUID userId) {
        jpaRepository.deleteByUserId(userId);
    }
}
