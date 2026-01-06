package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class UserAlreadyActivatedException extends DomainException {

    public UserAlreadyActivatedException(String message) {
        super(message);
    }
}
