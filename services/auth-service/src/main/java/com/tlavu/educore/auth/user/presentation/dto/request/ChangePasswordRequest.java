package com.tlavu.educore.auth.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(

    @NotNull(message = "Current password is required")
    @NotBlank(message = "Current password cannot be blank")
    String currentPassword,

    @NotNull(message = "New password is required")
    @NotBlank(message = "New password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,64}$",
            message = "Password must contain at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character"
    )
    String newPassword,

    @NotNull(message = "Confirm password is required")
    @NotBlank(message = "Confirm password cannot be blank")
    String confirmPassword
) {}
