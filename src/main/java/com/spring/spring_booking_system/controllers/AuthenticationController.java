package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.LoginUserDto;
import com.spring.spring_booking_system.dtos.RegisterUserDto;
import com.spring.spring_booking_system.dtos.UserResponseDto;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.responses.LoginResponse;
import com.spring.spring_booking_system.services.AuthenticationService;
import com.spring.spring_booking_system.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private JwtService jwtService;
    private AuthenticationService authenticationService;

    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService
    ) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(new UserResponseDto(registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        User loggedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(loggedUser);
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
