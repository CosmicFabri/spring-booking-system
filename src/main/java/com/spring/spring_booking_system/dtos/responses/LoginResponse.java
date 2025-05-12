package com.spring.spring_booking_system.dtos.responses;

import com.spring.spring_booking_system.dtos.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginResponse {
    private String token;
    private UserDto user;
    private long expiresIn;
}
