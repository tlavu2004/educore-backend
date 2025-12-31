package com.tlavu.educore.auth.authentication.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class InvalidRefreshTokenException extends DomainException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
