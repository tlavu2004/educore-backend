package com.tlavu.educore.auth.user.domain.exception;

public class AccountNotActivatedException extends RuntimeException {

    public AccountNotActivatedException(String message) {
        super(message);
    }
}
