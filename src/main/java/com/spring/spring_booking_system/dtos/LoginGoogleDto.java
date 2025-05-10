package com.spring.spring_booking_system.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class LoginGoogleDto {
    @JsonProperty("token")
    @NotNull(message = "Token is mandatory.")
    public String googleToken;
}
