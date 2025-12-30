package com.tlavu.educore.auth.user.application.usecase.handler;

import com.tlavu.educore.auth.shared.application.security.AuthContext;
import com.tlavu.educore.auth.user.application.usecase.command.ChangePasswordCommand;
import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.exception.InvalidOldPasswordException;
import com.tlavu.educore.auth.user.domain.exception.SamePasswordException;
import com.tlavu.educore.auth.user.domain.exception.UserNotFoundException;
import com.tlavu.educore.auth.user.domain.repository.UserRepository;
import com.tlavu.educore.auth.user.domain.valueobject.HashedPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.UUID;

@RequiredArgsConstructor
public class ChangePasswordHandler {

    private final UserRepository userRepository;
    private final AuthContext authContext;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Transactional
    public void handle(ChangePasswordCommand command) {
        // Get current authenticated user
        UUID currentUserId = authContext.getCurrentUserId()
            .orElseThrow(() -> new UserNotFoundException("No authenticated user"));

        // Authorization check
        if (!currentUserId.equals(command.userId())) {
            throw new AccessDeniedException("Cannot change another user's password");
        }

        // Load user
        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Verify old password
        if (!passwordEncoder.matches(command.oldPassword(), user.getHashedPassword().hashedValue())) {
            throw new InvalidOldPasswordException("Old password does not match");
        }

        // Check new password is different from old password
        if (passwordEncoder.matches(command.newPassword(), user.getHashedPassword().hashedValue())){
            throw new SamePasswordException("New password must be different from old password");
        }

        // Hash new password
        HashedPassword newHashedPassword = HashedPassword.fromRaw(
                command.newPassword(),
                passwordEncoder::encode
        );

        // Update password
        user.updatePassword(newHashedPassword, clock);

        // Optional: Revoke all refresh tokens when the password is changed
        // refreshTokenService.revokeAllTokensForUser(user.getId(), "password_changed");
    }
}
