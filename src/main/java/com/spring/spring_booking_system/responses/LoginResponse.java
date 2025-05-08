package com.spring.spring_booking_system.responses;

import lombok.*;

@Getter
@Setter
@ToString
public class LoginResponse {
    private String token;
    private long expiresIn;
}