package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class AccountNotActivatedException extends DomainException {

    public AccountNotActivatedException(String message) {
        super(message);
    }
}
