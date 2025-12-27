package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(String message) {
        super(message);
    }
}

