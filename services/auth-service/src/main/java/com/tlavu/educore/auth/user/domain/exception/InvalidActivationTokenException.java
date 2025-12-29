package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class InvalidActivationTokenException extends DomainException {

    public InvalidActivationTokenException(String message) {
        super(message);
    }
}
