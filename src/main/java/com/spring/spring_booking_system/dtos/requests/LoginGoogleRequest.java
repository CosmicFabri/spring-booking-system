package com.spring.spring_booking_system.dtos.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginGoogleRequest {
    @JsonProperty("token")
    @NotBlank(message = "Token is mandatory.")
    public String googleToken;
}
