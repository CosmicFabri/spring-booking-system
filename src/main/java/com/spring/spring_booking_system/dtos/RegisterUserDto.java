package com.spring.spring_booking_system.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    @JsonProperty("idRol")
    private Long roleId;
    private String email;
    private String password;
    private String fullName;
}