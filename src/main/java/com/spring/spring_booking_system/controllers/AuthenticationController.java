package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.requests.LoginGoogleRequest;
import com.spring.spring_booking_system.dtos.requests.LoginRequest;
import com.spring.spring_booking_system.dtos.requests.RegisterRequest;
import com.spring.spring_booking_system.dtos.UserDto;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.dtos.responses.LoginResponse;
import com.spring.spring_booking_system.services.AuthenticationService;
import com.spring.spring_booking_system.services.JwtService;
import com.spring.spring_booking_system.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private TokenService tokenService;

    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService,
            TokenService tokenService
    ) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        User loggedUser = authenticationService.authenticate(loginRequest);
        String jwtToken = jwtService.generateToken(loggedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setUser(new UserDto(loggedUser));

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login/google")
    public ResponseEntity<LoginResponse> googleLogin(@Valid @RequestBody LoginGoogleRequest loginGoogleRequest) {
        String googleToken = loginGoogleRequest.googleToken;

        User loggedUser = authenticationService.authenticateWithGoogleToken(googleToken);

        String jwtToken = jwtService.generateToken(loggedUser);
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setUser(new UserDto(loggedUser));


        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        final String jwt = authHeader.substring(7);

        tokenService.revokeToken(jwt);

        return ResponseEntity.ok().body(Map.of("token", jwt));
    }
}
