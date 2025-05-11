package com.spring.spring_booking_system.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {super("User not found");}

    public UserNotFoundException(Long userId) {
        super("User with ID " + userId + " not found");
    }
}
