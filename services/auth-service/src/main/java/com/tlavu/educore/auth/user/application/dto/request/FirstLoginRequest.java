package com.tlavu.educore.auth.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FirstLoginRequest(

    @NotNull(message = "Activation token is required")
    @NotBlank(message = "Activation token cannot be blank")
    String activationToken,

    @NotNull(message = "New password is required")
    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,64}$",
            message = "Password must contain at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character"
    )
    String newPassword,

    @NotNull(message = "Confirm password is required")
    @NotBlank(message = "Confirm password cannot be blank")
    String confirmPassword
) {}

