package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.dtos.requests.RegisterRequest;
import com.spring.spring_booking_system.entities.Role;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User register(RegisterRequest request) {
        User user = new User();

        if (!request.getEmail().endsWith("@uacam.mx")) {
            throw new IllegalArgumentException("Email must be from @uacam.mx domain");
        }

        if(userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("Email already in use");
        }

        Role role = roleService.findByName("user");
        user.setRole(role);

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }
}
