package com.tlavu.educore.auth.authentication.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class RefreshTokenRevokedException extends DomainException {

    public RefreshTokenRevokedException(String message) {
        super(message);
    }
}
