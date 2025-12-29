package com.tlavu.educore.auth.authentication.application.service.interfaces;

import com.tlavu.educore.auth.user.domain.entity.User;

import java.time.Instant;
import java.util.UUID;

public interface AccessTokenService {

    public String generate(User user);
    public boolean validate(String token);
    public UUID extractUserId(String token);
    public Instant extractExpiration(String token);
}
