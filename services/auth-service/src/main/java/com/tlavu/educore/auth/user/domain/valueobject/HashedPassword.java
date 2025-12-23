package com.tlavu.educore.auth.user.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NonNull;

import java.io.Serializable;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.Objects;
import java.util.regex.Pattern;

public record HashedPassword(@JsonIgnore String hashedValue) implements Serializable {

    // --- Regex patterns ---
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 64;
    private static final String SPECIAL_CHARS = "@$!%*?&";
    // When needing expansion, consider moving password policy to application layer.
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[" + SPECIAL_CHARS + "])[A-Za-z\\d" + SPECIAL_CHARS + "]{" + MIN_LENGTH + "," + MAX_LENGTH + "}$"
    );

    // --- Constructors ---
    public HashedPassword {
        Objects.requireNonNull(hashedValue, "hashed password cannot be null.");
        requireNonBlank(hashedValue, "hashed password");
    }

    // --- Static factory methods ---
    public static HashedPassword fromHashed(String hashedValue) {
        return new HashedPassword(hashedValue);
    }

    public static HashedPassword fromRaw(String rawValue, Function<String, String> encoder) {
        Objects.requireNonNull(rawValue, "raw password cannot be null.");
        Objects.requireNonNull(encoder, "encoder function cannot be null.");

        requireValidPassword(rawValue);

        String hashedPassword = encoder.apply(rawValue);
        return new HashedPassword(hashedPassword);
    }

    // --- Business methods ---
    public boolean matches(String rawValue, BiPredicate<String, String> matcher) {
        Objects.requireNonNull(rawValue, "raw password cannot be null.");
        Objects.requireNonNull(matcher, "matcher cannot be null.");

        return matcher.test(rawValue, this.hashedValue);
    }

    // --- Overrides ---
    @Override
    @NonNull
    public String toString() {
        return "********";
    }

    // --- Private helpers ---
    @SuppressWarnings("SameParameterValue")
    private static void requireNonBlank(String value, String fieldName) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
    }

    private static void requireValidPassword(String value) {
        if (!PASSWORD_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid password format. Password must be 8–64 characters long, contain at least 1 uppercase, 1 lowercase, 1 number, and 1 special character."
            );
        }
    }
}