package com.spring.spring_booking_system.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long roleId) {
        super("Role with ID " + roleId + " not found");
    }
}
