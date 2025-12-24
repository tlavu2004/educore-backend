package com.tlavu.educore.auth.user.domain.exception;

public class ActivationTokenExpiredException extends RuntimeException {

    public ActivationTokenExpiredException(String message) {
        super(message);
    }
}
