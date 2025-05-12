package com.spring.spring_booking_system.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email is mandatory.")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is mandatory.")
    private String password;
}