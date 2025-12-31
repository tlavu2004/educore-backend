package com.tlavu.educore.auth.shared.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Error details for API error responses
 * Provides structured error information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDetails(
        String code,
        String message,
        String details
) {
    /**
     * Create error details with code and message
     */
    public static ErrorDetails of(String code, String message) {
        return new ErrorDetails(code, message, null);
    }

    /**
     * Create error details with all fields
     */
    public static ErrorDetails of(String code, String message, String details) {
        return new ErrorDetails(code, message, details);
    }
}

