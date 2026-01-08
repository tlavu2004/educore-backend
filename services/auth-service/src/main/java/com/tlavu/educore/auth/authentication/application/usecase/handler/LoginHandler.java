package com.tlavu.educore.auth.authentication.application.usecase.handler;

import com.tlavu.educore.auth.authentication.presentation.dto.response.AuthResult;
import com.tlavu.educore.auth.authentication.application.service.interfaces.AccessTokenService;
import com.tlavu.educore.auth.authentication.application.service.interfaces.RefreshTokenService;
import com.tlavu.educore.auth.authentication.application.usecase.command.LoginCommand;
import com.tlavu.educore.auth.authentication.domain.entity.RefreshToken;
import com.tlavu.educore.auth.authentication.domain.exception.InvalidCredentialsException;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;

@RequiredArgsConstructor
@Service
public class LoginHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final Clock clock;

    @Transactional
    public AuthResult handle(LoginCommand command) {
        Email email = Email.of(command.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!user.getHashedPassword().matches(
                command.password(),
                passwordEncoder::matches
        )) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        user.recordLogin(clock);

        String accessToken = accessTokenService.generate(
                user.getId().value(),
                user.getEmail().value(),
                user.getRole().name()
        );

        RefreshToken refreshToken = refreshTokenService.issue(user.getId().value());

        return new AuthResult(
                accessToken,
                refreshToken.getRefreshTokenValue().value(),
                accessTokenService.extractExpiration(accessToken).getEpochSecond()
        );
    }
}
