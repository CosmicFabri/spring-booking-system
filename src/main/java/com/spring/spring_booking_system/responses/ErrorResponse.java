package com.spring.spring_booking_system.responses;

import lombok.*;

@Getter
@Setter
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
