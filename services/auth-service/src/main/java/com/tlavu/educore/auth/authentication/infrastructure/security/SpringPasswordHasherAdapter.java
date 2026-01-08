package com.tlavu.educore.auth.authentication.infrastructure.security;

import com.tlavu.educore.auth.authentication.domain.port.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringPasswordHasherAdapter implements PasswordHasher {

    private final PasswordEncoder encoder;

    public SpringPasswordHasherAdapter(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}

