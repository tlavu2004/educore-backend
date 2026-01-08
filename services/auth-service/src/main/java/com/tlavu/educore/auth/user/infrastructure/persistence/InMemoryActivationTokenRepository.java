package com.tlavu.educore.auth.user.infrastructure.persistence;

import com.tlavu.educore.auth.user.domain.entity.ActivationToken;
import com.tlavu.educore.auth.user.domain.repository.ActivationTokenRepository;
import com.tlavu.educore.auth.user.domain.valueobject.ActivationTokenValue;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryActivationTokenRepository implements ActivationTokenRepository {

    private final Map<UUID, ActivationToken> byId = new ConcurrentHashMap<>();
    private final Map<String, ActivationToken> byToken = new ConcurrentHashMap<>();

    @Override
    public ActivationToken save(ActivationToken token) {
        byId.put(token.getId(), token);
        byToken.put(token.getActivationTokenValue().toString(), token);
        return token;
    }

    @Override
    public Optional<ActivationToken> findByToken(ActivationTokenValue token) {
        return Optional.ofNullable(byToken.get(token.toString()));
    }

    @Override
    public Optional<ActivationToken> findByUserId(UUID userId) {
        return byId.values().stream().filter(t -> t.getUserId().value().equals(userId)).findFirst();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        byId.values().removeIf(t -> t.getUserId().value().equals(userId));
        byToken.values().removeIf(t -> t.getUserId().value().equals(userId));
    }
}

