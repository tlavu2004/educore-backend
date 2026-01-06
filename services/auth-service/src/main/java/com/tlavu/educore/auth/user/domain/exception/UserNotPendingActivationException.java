package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class UserNotPendingActivationException extends DomainException {

    public UserNotPendingActivationException(String message) {
        super(message);
    }
}
