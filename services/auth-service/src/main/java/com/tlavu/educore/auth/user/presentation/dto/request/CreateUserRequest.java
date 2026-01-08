package com.tlavu.educore.auth.user.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record CreateUserRequest(

    @NotNull(message = "Email is required")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    String email,

    @NotNull(message = "Full name is required")
    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 1, max = 100, message = "Full name must be between 1 and 100 characters")
    String fullName,

    @NotNull(message = "Role is required")
    @NotBlank(message = "Full name cannot be blank")
    String role
) {}
