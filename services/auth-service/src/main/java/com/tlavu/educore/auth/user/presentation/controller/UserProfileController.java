package com.tlavu.educore.auth.user.presentation.controller;

import com.tlavu.educore.auth.user.application.usecase.query.GetUserProfileQuery;
import com.tlavu.educore.auth.user.application.usecase.handler.GetUserProfileHandler;
import com.tlavu.educore.auth.user.presentation.dto.response.UserProfileResponse;
import com.tlavu.educore.auth.user.application.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {

    private final GetUserProfileHandler getUserProfileHandler;
    private final UserMapper userMapper;

    public UserProfileController(GetUserProfileHandler getUserProfileHandler, UserMapper userMapper) {
        this.getUserProfileHandler = getUserProfileHandler;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me() {
        // TODO: obtain current user id from security context; for now we return a placeholder
        UUID currentUserId = UUID.randomUUID();
        var resp = getUserProfileHandler.handle(new GetUserProfileQuery(currentUserId));
        return ResponseEntity.ok(resp);
    }
}

