package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.LoginUserDto;
import com.spring.spring_booking_system.dtos.RegisterUserDto;
import com.spring.spring_booking_system.dtos.UserResponseDto;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.exceptions.RoleNotFoundException;
import com.spring.spring_booking_system.responses.LoginResponse;
import com.spring.spring_booking_system.responses.ErrorResponse;
import com.spring.spring_booking_system.services.AuthenticationService;
import com.spring.spring_booking_system.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterUserDto registerUserDto) {
        try {
            User registeredUser = authenticationService.signup(registerUserDto);
            return ResponseEntity.ok(new UserResponseDto(registeredUser));
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Role not found"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        try {
            User loggedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(loggedUser);
            LoginResponse loginResponse = new LoginResponse();

            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Fill all the fields."));
        }
    }
}
