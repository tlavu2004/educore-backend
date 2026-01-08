package com.tlavu.educore.auth.authentication.application.usecase.handler;

import com.tlavu.educore.auth.authentication.application.service.interfaces.AccessTokenService;
import com.tlavu.educore.auth.authentication.application.usecase.query.GetAuthenticatedUserQuery;
import com.tlavu.educore.auth.user.presentation.dto.response.UserResponse;
import com.tlavu.educore.auth.user.application.mapper.UserMapper;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.UserNotFoundException;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

// TODO: Synchronize @Component/@Service usage across handlers (later)
@RequiredArgsConstructor
public class GetAuthenticatedUserHandler {

    private final AccessTokenService accessTokenService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse handle(GetAuthenticatedUserQuery query) {
        UUID userId = accessTokenService.extractUserId(query.accessToken());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return userMapper.toResponse(user);
    }
}
