package com.tlavu.educore.auth.authentication.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class RefreshTokenExpiredException extends DomainException {

    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
