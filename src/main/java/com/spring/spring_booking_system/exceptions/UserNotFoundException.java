package com.spring.spring_booking_system.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {super("User not found");}
}
