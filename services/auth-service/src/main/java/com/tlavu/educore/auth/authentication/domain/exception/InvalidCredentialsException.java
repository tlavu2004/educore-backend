package com.tlavu.educore.auth.authentication.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}

