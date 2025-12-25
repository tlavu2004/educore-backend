package com.tlavu.educore.auth.user.domain.exception;

import com.tlavu.educore.auth.shared.domain.exception.DomainException;

public class AccountSuspendedException extends DomainException {
    public AccountSuspendedException(String message) {
        super(message);
    }
}
