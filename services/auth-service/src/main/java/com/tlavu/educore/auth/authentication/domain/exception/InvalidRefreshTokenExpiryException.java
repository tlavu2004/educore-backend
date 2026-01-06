package com.tlavu.educore.auth.authentication.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class InvalidRefreshTokenExpiryException extends DomainException {

    public InvalidRefreshTokenExpiryException(String message) {
        super(message);
    }
}
