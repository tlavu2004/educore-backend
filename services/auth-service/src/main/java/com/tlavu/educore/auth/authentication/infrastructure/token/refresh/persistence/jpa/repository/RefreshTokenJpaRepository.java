package com.tlavu.educore.auth.authentication.infrastructure.token.refresh.persistence.jpa.repository;

import com.tlavu.educore.auth.authentication.infrastructure.token.refresh.persistence.jpa.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, UUID> {

    Optional<RefreshTokenJpaEntity> findByToken(String token);
    Optional<RefreshTokenJpaEntity> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
    void deleteByToken(String token);
}

