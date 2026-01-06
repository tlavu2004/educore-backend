package com.tlavu.educore.auth.authentication.application.usecase.handler;

import com.tlavu.educore.auth.authentication.application.dto.response.AuthResult;
import com.tlavu.educore.auth.authentication.application.service.interfaces.AccessTokenService;
import com.tlavu.educore.auth.authentication.application.service.interfaces.RefreshTokenService;
import com.tlavu.educore.auth.authentication.application.usecase.command.RefreshTokenCommand;
import com.tlavu.educore.auth.authentication.domain.entity.RefreshToken;
import com.tlavu.educore.auth.authentication.domain.valueobject.RefreshTokenValue;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.UserNotFoundException;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class RefreshTokenHandler {

    private final UserRepository userRepository;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResult handler(RefreshTokenCommand command) {
        RefreshTokenValue refreshTokenValue = RefreshTokenValue.of(command.refreshToken());

        RefreshToken newRefreshToken = refreshTokenService.rotate(refreshTokenValue);

        User user = userRepository.findById(newRefreshToken.getUserId().value())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + newRefreshToken.getUserId()));

        String accessToken = accessTokenService.generate(
                user.getId().value(),
                user.getEmail().value(),
                user.getRole().name()
        );

        return new AuthResult(
                accessToken,
                newRefreshToken.getRefreshTokenValue().value(),
                accessTokenService.extractExpiration(accessToken).getEpochSecond()
        );
    }
}
