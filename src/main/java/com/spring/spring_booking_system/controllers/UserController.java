package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.UserDto;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.exceptions.UserNotFoundException;
import com.spring.spring_booking_system.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserDto getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = (Long) authentication.getPrincipal();

        User user = userService.findById(id).orElse(null);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return new UserDto(user);
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        String fullName = currentUser.getFullName();

        return fullName + "'s Dashboard";
    }
}
