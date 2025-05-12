package com.spring.spring_booking_system.exceptions;

public class SpaceNotFoundException extends RuntimeException {
    public SpaceNotFoundException() {super("Space not found");}
}
