package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}


