package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class ActivationTokenExpiredException extends DomainException {

    public ActivationTokenExpiredException(String message) {
        super(message);
    }
}
