package com.spring.spring_booking_system.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    @NotNull(message = "Email is mandatory.")
    private String email;

    @NotNull(message = "Password is mandatory.")
    private String password;
}