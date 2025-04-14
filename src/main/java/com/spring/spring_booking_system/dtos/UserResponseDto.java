package com.spring.spring_booking_system.dtos;

import com.spring.spring_booking_system.entities.User;

public class UserResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String role;

    public UserResponseDto(User user) {
        this.id = user.getId().longValue();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole().getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}