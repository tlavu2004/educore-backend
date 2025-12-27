package com.tlavu.educore.auth.shared.application.security;

import java.util.Optional;
import java.util.UUID;

public interface AuthContext {

    Optional<UUID> getCurrentUserId();
}
