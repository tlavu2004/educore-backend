package com.tlavu.educore.auth.user.presentation.controller;

import com.tlavu.educore.auth.shared.application.security.AuthContext;
import com.tlavu.educore.auth.user.presentation.dto.request.CreateUserRequest;
import com.tlavu.educore.auth.user.presentation.dto.request.ChangePasswordRequest;
import com.tlavu.educore.auth.user.presentation.dto.request.ActivateUserRequest;
import com.tlavu.educore.auth.user.application.usecase.command.CreateUserCommand;
import com.tlavu.educore.auth.user.application.usecase.command.FirstLoginCommand;
import com.tlavu.educore.auth.user.application.usecase.command.ChangePasswordCommand;
import com.tlavu.educore.auth.user.application.usecase.command.ActivateUserCommand;
import com.tlavu.educore.auth.user.application.usecase.handler.CreateUserHandler;
import com.tlavu.educore.auth.user.application.usecase.handler.FirstLoginHandler;
import com.tlavu.educore.auth.user.application.usecase.handler.ChangePasswordHandler;
import com.tlavu.educore.auth.user.application.usecase.handler.ActivateUserHandler;
import com.tlavu.educore.auth.user.presentation.dto.response.UserResponse;
import com.tlavu.educore.auth.user.application.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserHandler createUserHandler;
    private final FirstLoginHandler firstLoginHandler;
    private final ChangePasswordHandler changePasswordHandler;
    private final ActivateUserHandler activateUserHandler;
    private final UserMapper userMapper;

    @Autowired(required = false)
    private AuthContext authContext;

    public UserController(CreateUserHandler createUserHandler,
                          FirstLoginHandler firstLoginHandler,
                          ChangePasswordHandler changePasswordHandler,
                          ActivateUserHandler activateUserHandler,
                          UserMapper userMapper) {
        this.createUserHandler = createUserHandler;
        this.firstLoginHandler = firstLoginHandler;
        this.changePasswordHandler = changePasswordHandler;
        this.activateUserHandler = activateUserHandler;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Validated @RequestBody CreateUserRequest req) {
        // Create a secure temporary password for the new user (will be part of activation flow)
        String tempPassword = generateTemporaryPassword();
        CreateUserCommand cmd = new CreateUserCommand(req.email(), tempPassword, req.fullName());
        var created = createUserHandler.handle(cmd);
        return ResponseEntity.ok(userMapper.toResponse(created));
    }

    @PostMapping("/first-login")
    public ResponseEntity<Void> firstLogin() {
        if (authContext == null) throw new IllegalStateException("AuthContext unavailable");
        UUID currentUserId = authContext.getCurrentUserId().orElseThrow(() -> new IllegalStateException("No authenticated user"));
        FirstLoginCommand cmd = new FirstLoginCommand(currentUserId);
        firstLoginHandler.handle(cmd);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Validated @RequestBody ChangePasswordRequest req) {
        if (authContext == null) throw new IllegalStateException("AuthContext unavailable");
        UUID currentUserId = authContext.getCurrentUserId().orElseThrow(() -> new IllegalStateException("No authenticated user"));
        ChangePasswordCommand cmd = new ChangePasswordCommand(currentUserId, req.currentPassword(), req.newPassword());
        changePasswordHandler.handle(cmd);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@Validated @RequestBody ActivateUserRequest req) {
        ActivateUserCommand cmd = new ActivateUserCommand(req.token());
        activateUserHandler.handle(cmd);
        return ResponseEntity.ok().build();
    }

    // --- Helpers ---
    private static String generateTemporaryPassword() {
        // Generate a URL-safe base64 string and then ensure it contains required char classes.
        // For brevity generate 12 bytes -> base64 length ~16, then tweak to satisfy policy if needed.
        SecureRandom rnd = new SecureRandom();
        byte[] buf = new byte[12];
        rnd.nextBytes(buf);
        String candidate = Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
        // Ensure contains at least 1 upper, 1 lower, 1 digit, 1 special
        if (!candidate.matches(".*[A-Z].*")) candidate = "A" + candidate;
        if (!candidate.matches(".*[a-z].*")) candidate = "a" + candidate;
        if (!candidate.matches(".*\\d.*")) candidate = "1" + candidate;
        if (!candidate.matches(".*[@$!%*?&].*")) candidate = "@" + candidate;
        return candidate;
    }
}
