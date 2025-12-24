package com.tlavu.educore.auth.user.domain.valueobject;

import lombok.NonNull;

import java.util.Objects;
import java.util.UUID;

public record ActivationTokenValue(String value) {

    // --- Constructors ---
    public ActivationTokenValue {
        Objects.requireNonNull(value, "activation token cannot be null");
        requireNonBlank(value, "activation token");
    }

    // --- Static factory methods ---
    public static ActivationTokenValue of(String value) {
        return new ActivationTokenValue(value);
    }

    // --- Business methods ---
    public static ActivationTokenValue generate() {
        return new ActivationTokenValue(UUID.randomUUID().toString());
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
