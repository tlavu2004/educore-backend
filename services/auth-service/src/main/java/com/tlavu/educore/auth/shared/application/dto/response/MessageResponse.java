package com.tlavu.educore.auth.shared.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Standard message response for simple operations
 * Used for operations that only need to return a success message
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MessageResponse(
        String message,
        boolean success,
        Instant timestamp
) {
    /**
     * Compact constructor with defaults
     */
    public MessageResponse {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    /**
     * Create a success message response
     */
    public static MessageResponse success(String message) {
        return new MessageResponse(message, true, Instant.now());
    }

    /**
     * Create a failure message response
     */
    public static MessageResponse failure(String message) {
        return new MessageResponse(message, false, Instant.now());
    }

    /**
     * Create a success message response with custom timestamp
     */
    public static MessageResponse success(String message, Instant timestamp) {
        return new MessageResponse(message, true, timestamp);
    }

    /**
     * Create a failure message response with custom timestamp
     */
    public static MessageResponse failure(String message, Instant timestamp) {
        return new MessageResponse(message, false, timestamp);
    }
}


