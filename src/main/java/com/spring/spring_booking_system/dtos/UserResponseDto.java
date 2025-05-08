package com.spring.spring_booking_system.dtos;

import com.spring.spring_booking_system.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}