package com.spring.spring_booking_system.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    @JsonProperty("idRol")
    @NotNull(message = "Role ID is mandatory.")
    private Long roleId;

    @NotBlank(message = "Email is mandatory.")
    private String email;

    @NotBlank(message = "Password is mandatory.")
    private String password;

    @NotBlank(message = "Full name is mandatory.")
    private String fullName;
}