package com.tlavu.educore.auth.user.application.usecase.handler;

import com.tlavu.educore.auth.user.application.usecase.command.FirstLoginCommand;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.UserNotFoundException;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@RequiredArgsConstructor
public class FirstLoginHandler {

    private final UserRepository userRepository;
    private final Clock clock;

    @Transactional
    public void handle(FirstLoginCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.completeFirstLogin(clock);
    }
}
