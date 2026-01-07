package com.tlavu.educore.auth.user.infrastructure.adapter.persistence;

import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import com.tlavu.educore.auth.user.infrastructure.persistence.jpa.entity.UserJpaEntity;
import com.tlavu.educore.auth.user.infrastructure.persistence.jpa.mapper.UserJpaMapper;
import com.tlavu.educore.auth.user.infrastructure.persistence.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserPersistenceAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserJpaMapper mapper;

    public UserPersistenceAdapter(UserJpaRepository jpaRepository, UserJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}

