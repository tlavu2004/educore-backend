package com.tlavu.educore.auth.user.application.usecase.handler;

import com.tlavu.educore.auth.user.presentation.dto.response.UserProfileResponse;
import com.tlavu.educore.auth.user.application.usecase.query.GetUserProfileQuery;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.UserNotFoundException;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class GetUserProfileHandler {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse handle(GetUserProfileQuery query) {
        User user = userRepository.findById(query.userId())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + query.userId()
                ));

        return UserProfileResponse.fromEntity(user);
    }
}
