package com.tlavu.educore.auth.authentication.domain.valueobject;

import lombok.NonNull;

import java.util.Objects;
import java.util.UUID;

public record RefreshTokenValue(String value) {

    // --- Constructors ---
    public RefreshTokenValue {
        Objects.requireNonNull(value, "refreshToken cannot be null");
        requireNonBlank(value, "refreshToken");
    }

    // --- Static factory methods ---
    public static RefreshTokenValue of(String value) {
        return new RefreshTokenValue(value);
    }

    // --- Business methods ---
    public static RefreshTokenValue generate() {
        return new RefreshTokenValue(UUID.randomUUID().toString());
    }

    // --- Overrides ---
    @Override
    @NonNull
    public String toString() {
        return value;
    }

    // --- Private helpers ---
    @SuppressWarnings("SameParameterValue")
    private static void requireNonBlank(String value, String fieldName) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
    }
}
