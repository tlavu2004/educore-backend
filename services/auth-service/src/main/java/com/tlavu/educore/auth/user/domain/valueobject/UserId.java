package com.tlavu.educore.auth.user.domain.valueobject;

import lombok.NonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) implements Serializable {

    // --- Constructors ---
    public UserId {
        Objects.requireNonNull(value, "userId cannot be null");
    }

    // --- Static factory methods ---

    public static UserId of(UUID value) {
        return new UserId(value);
    }

    public static UserId fromString(String rawValue) {
        try {
            return new UserId(UUID.fromString(rawValue));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid UserId format: " + rawValue, ex
            );
        }
    }

    // --- Business methods ---
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    // --- Convenience ---
    @Override
    @NonNull
    public String toString() {
        return value.toString();
    }
}
