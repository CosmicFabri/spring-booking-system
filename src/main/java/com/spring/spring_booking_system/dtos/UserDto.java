package com.spring.spring_booking_system.dtos;

import com.spring.spring_booking_system.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String role;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole().getName();
    }
}