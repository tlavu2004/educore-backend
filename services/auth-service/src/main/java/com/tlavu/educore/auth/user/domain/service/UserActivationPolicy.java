package com.tlavu.educore.auth.user.domain.service;

import com.tlavu.educore.auth.user.domain.entity.User;

import java.util.Objects;

public class UserActivationPolicy {

    public void ensureUserIsActivated(User user) {
        Objects.requireNonNull(user, "user must not be null");

        if (!user.isActivated()) {
            throw new RuntimeException("Account is not activated.");
        }
    }
}
