package com.tlavu.educore.auth.user.application.usecase.handler;

import com.tlavu.educore.auth.shared.application.security.AuthContext;
import com.tlavu.educore.auth.user.application.usecase.command.CreateUserCommand;
import com.tlavu.educore.auth.user.domain.entity.ActivationToken;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.repository.ActivationTokenRepository;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import com.tlavu.educore.auth.user.domain.valueobject.ActivationTokenValue;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import com.tlavu.educore.auth.user.domain.valueobject.HashedPassword;
import com.tlavu.educore.auth.user.domain.valueobject.UserId;
import com.tlavu.educore.auth.authentication.domain.port.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CreateUserHandler {

    private final UserRepository userRepository;
    private final ActivationTokenRepository activationTokenRepository;
    private final PasswordHasher passwordHasher;
    private final AuthContext authContext;
    private final Clock clock;

    @Transactional
    public User handle(CreateUserCommand command) {
        HashedPassword hashedPassword = HashedPassword.fromRaw(
                command.rawPassword(),
                passwordHasher::hash
        );

        UUID createdById = authContext.getCurrentUserId().orElse(null);

        User user = User.createNew(
                new Email(command.email()),
                hashedPassword,
                command.fullName(),
                UserRole.ADMIN,
                UserId.of(createdById),
                clock
        );

        User saved = userRepository.save(user);

        // Create and persist activation token (default TTL 7 days)
        Instant expiresAt = Instant.now(clock).plus(Duration.ofDays(7));
        ActivationToken activationToken = ActivationToken.createNew(
                ActivationTokenValue.generate(),
                UserId.of(saved.getId().value()),
                expiresAt,
                clock
        );

        ActivationToken savedToken = activationTokenRepository.save(activationToken);

        // Note: domain event publishing removed (ActivationTokenCreatedEvent not found in codebase)

        return saved;
    }
}
