package com.tlavu.educore.auth.user.application.usecase.handler;

import com.tlavu.educore.auth.user.application.usecase.command.ActivateUserCommand;
import com.tlavu.educore.auth.user.domain.entity.ActivationToken;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.InvalidActivationTokenException;
import com.tlavu.educore.auth.user.domain.exception.UserNotFoundException;
import com.tlavu.educore.auth.user.domain.repository.ActivationTokenRepository;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import com.tlavu.educore.auth.user.domain.valueobject.ActivationTokenValue;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@RequiredArgsConstructor
public class ActivateUserHandler {

    private final UserRepository userRepository;
    private final ActivationTokenRepository activationTokenRepository;

    @Transactional
    public void handle(ActivateUserCommand command) {
        ActivationToken activationToken = activationTokenRepository
            .findByToken(new ActivationTokenValue(command.activationToken()))
            .orElseThrow(() -> new InvalidActivationTokenException("Invalid activation token"));

        activationToken.markAsUsed(Instant.now());

        User user = userRepository.findById(activationToken.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.activate();
    }
}
