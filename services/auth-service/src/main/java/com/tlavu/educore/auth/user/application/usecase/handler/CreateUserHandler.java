package com.tlavu.educore.auth.user.application.usecase.handler;

import com.tlavu.educore.auth.shared.application.security.AuthContext;
import com.tlavu.educore.auth.user.application.usecase.command.CreateUserCommand;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import com.tlavu.educore.auth.user.domain.valueobject.HashedPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateUserHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthContext authContext;

    @Transactional
    public User handle(CreateUserCommand command) {
        HashedPassword hashedPassword = HashedPassword.fromRaw(
                command.rawPassword(),
                passwordEncoder::encode
        );

        UUID createdById = authContext.getCurrentUserId().orElse(null);

        User user = User.createNew(
                new Email(command.email()),
                hashedPassword,
                command.fullName(),
                UserRole.ADMIN,
                createdById
        );

        return userRepository.save(user);
    }
}
