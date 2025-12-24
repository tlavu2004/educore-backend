package com.tlavu.educore.auth.user.domain.exception;

public class ActivationTokenAlreadyUsedException extends RuntimeException {

    public ActivationTokenAlreadyUsedException(String message) {
        super(message);
    }
}
