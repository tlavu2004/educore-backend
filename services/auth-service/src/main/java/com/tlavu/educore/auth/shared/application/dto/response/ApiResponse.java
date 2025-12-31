package com.tlavu.educore.auth.shared.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        ErrorDetails error,
        Instant timestamp,
        String path
) {
    public ApiResponse {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    // === Factory Methods - Success ===

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data, null, Instant.now(), null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, null, Instant.now(), null);
    }

    public static <T> ApiResponse<T> successMessage(String message) {
        return new ApiResponse<>(true, message, null, null, Instant.now(), null);
    }

    // === Factory Methods - Error ===

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, null, Instant.now(), null);
    }

    public static <T> ApiResponse<T> error(String message, String path) {
        return new ApiResponse<>(false, message, null, null, Instant.now(), path);
    }

    public static <T> ApiResponse<T> error(String message, ErrorDetails errorDetails) {
        return new ApiResponse<>(false, message, null, errorDetails, Instant.now(), null);
    }

    public static <T> ApiResponse<T> error(String code, String message, String details) {
        ErrorDetails errorDetails = ErrorDetails.of(code, message, details);
        return new ApiResponse<>(false, message, null, errorDetails, Instant.now(), null);
    }

    public static <T> ApiResponse<T> error(String message, ErrorDetails errorDetails, String path) {
        return new ApiResponse<>(false, message, null, errorDetails, Instant.now(), path);
    }

    // === Builder-style Methods ===

    public ApiResponse<T> withPath(String path) {
        return new ApiResponse<>(success, message, data, error, timestamp, path);
    }

    public ApiResponse<T> withTimestamp(Instant timestamp) {
        return new ApiResponse<>(success, message, data, error, timestamp, path);
    }
}



