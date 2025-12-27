package com.tlavu.educore.auth.user.domain.repository;

import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.valueobject.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    void delete(UUID id);
}

