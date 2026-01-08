package com.tlavu.educore.auth.user.application.usecase.handler;

import com.tlavu.educore.auth.user.presentation.dto.response.UserProfileResponse;
import com.tlavu.educore.auth.user.application.usecase.query.GetUserProfileQuery;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.UserNotFoundException;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import com.tlavu.educore.auth.user.application.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetUserProfileHandler {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserProfileResponse handle(GetUserProfileQuery query) {
        User user = userRepository.findById(query.userId())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + query.userId()
                ));

        return userMapper.toProfileResponse(user);
    }
}
