package com.tlavu.educore.auth.user.domain.valueobject;

import lombok.NonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) implements Serializable {

    // --- Regex patterns ---
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // --- Constructors ---
    public Email {
        Objects.requireNonNull(value, "email cannot be null");
        requireNonBlank(value, "email");

        value = normalizeEmail(value);

        requireValidEmailAddress(value);
    }

    // --- Static factory methods ---
    public static Email of(String value) {
        return new Email(value);
    }

    // --- Business methods ---
    public String domain() {
        return value.substring(value.indexOf('@') + 1);
    }

    public String localPart() {
        return value.substring(0, value.indexOf('@'));
    }

    public boolean isFromDomain(String domain) {
        return this.domain().equalsIgnoreCase(domain);
    }

    // --- Overrides ---
    @Override
    @NonNull
    public String toString() {
        return value;
    }

    // --- Private helpers ---
    private static String normalizeEmail(String rawValue) {
        String trimmedValue = rawValue.trim();
        int atIndex = trimmedValue.indexOf('@');
        if (atIndex < 1 || atIndex == trimmedValue.length() - 1) {
            throw new IllegalArgumentException("Email must contain local part and domain");
        }

        String localPart = trimmedValue.substring(0, atIndex);
        String domainPart = trimmedValue.substring(atIndex + 1).trim().toLowerCase();
        return localPart + "@" + domainPart;
    }

    private static void requireNonBlank(String value, String fieldName) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
    }

    private static void requireValidEmailAddress(String value) {
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email address format: " + value);
        }
    }
}