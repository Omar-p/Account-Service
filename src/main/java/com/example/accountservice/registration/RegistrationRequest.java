package com.example.accountservice.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull @NotEmpty String name,
        @NotNull @NotEmpty String lastname,
        @NotNull @NotEmpty @Email
        String email,
        @NotNull @NotEmpty String password
) {
}
