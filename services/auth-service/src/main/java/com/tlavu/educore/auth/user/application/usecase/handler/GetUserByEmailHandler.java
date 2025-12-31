package com.tlavu.educore.auth.user.application.usecase.handler;

import com.tlavu.educore.auth.user.application.usecase.query.GetUserByEmailQuery;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.UserNotFoundException;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class GetUserByEmailHandler {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User handle(GetUserByEmailQuery query) {
        Email email = Email.of(query.email());
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with email: " + query.email()
                ));
    }
}
