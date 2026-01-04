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
import com.tlavu.educore.auth.user.domain.event.ActivationTokenCreatedEvent;
import com.tlavu.educore.auth.shared.application.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateUserHandler {

    private final UserRepository userRepository;
    private final ActivationTokenRepository activationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthContext authContext;
    private final EventPublisher eventPublisher;
    private final Clock clock;

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
                createdById,
                clock
        );

        User saved = userRepository.save(user);

        // Create and persist activation token (default TTL 7 days)
        Instant expiresAt = Instant.now(clock).plus(Duration.ofDays(7));
        ActivationToken activationToken = ActivationToken.createNew(
                ActivationTokenValue.generate(),
                saved.getId(),
                expiresAt,
                clock
        );

        ActivationToken savedToken = activationTokenRepository.save(activationToken);

        // Publish event for infra to send email (listener will build link using frontend base URL)
        eventPublisher.publish(new ActivationTokenCreatedEvent(
                saved.getId(),
                saved.getEmail().toString(),
                savedToken.getActivationTokenValue().toString(),
                null, // redirectUrl — can be filled by caller or env in listener
                Instant.now(clock)
        ));

        return saved;
    }
}
