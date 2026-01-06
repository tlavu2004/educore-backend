package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class UserNotActivatedException extends DomainException {

    public UserNotActivatedException(String message) {
        super(message);
    }
}
