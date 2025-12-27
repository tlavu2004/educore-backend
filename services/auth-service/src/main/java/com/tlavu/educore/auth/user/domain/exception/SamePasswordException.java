package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class SamePasswordException extends DomainException {

    public SamePasswordException(String message) {
        super(message);
    }
}
