package com.spring.spring_booking_system.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super("User with ID " + userId + " not found");
    }
}
