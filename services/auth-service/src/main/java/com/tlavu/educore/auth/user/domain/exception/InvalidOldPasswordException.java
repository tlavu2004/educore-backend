package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class InvalidOldPasswordException extends DomainException {

    public InvalidOldPasswordException(String message) {
        super(message);
    }
}
