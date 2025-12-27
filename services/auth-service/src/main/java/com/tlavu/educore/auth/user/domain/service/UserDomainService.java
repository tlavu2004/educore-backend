package com.tlavu.educore.auth.user.domain.service;

import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.AccountNotActivatedException;
import com.tlavu.educore.auth.user.domain.exception.AccountSuspendedException;

public class UserDomainService {

    public void ensureUserCanLogin(User user) {
        if (user.isSuspended()) {
            throw new AccountSuspendedException("Account is suspended");
        }

        if (!user.isActivated()) {
            throw new AccountNotActivatedException("Account is not activated");
        }
    }
}
