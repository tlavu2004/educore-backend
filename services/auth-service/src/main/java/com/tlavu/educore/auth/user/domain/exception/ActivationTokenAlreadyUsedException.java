package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class ActivationTokenAlreadyUsedException extends DomainException {

    public ActivationTokenAlreadyUsedException(String message) {
        super(message);
    }
}
