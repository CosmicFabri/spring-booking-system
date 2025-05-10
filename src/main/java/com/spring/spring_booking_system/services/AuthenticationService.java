package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.dtos.requests.LoginRequest;
import com.spring.spring_booking_system.dtos.requests.RegisterRequest;
import com.spring.spring_booking_system.entities.Role;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.repositories.RoleRepository;
import com.spring.spring_booking_system.repositories.UserRepository;
import com.spring.spring_booking_system.exceptions.RoleNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(RegisterRequest input) {
        User user = new User();

        Role role = roleRepository.findById(input.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException(input.getRoleId()));

        user.setRole(role);
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public User authenticateWithGoogleToken(String googleToken) {
        // Le pedimos los datos de la cuenta a Google
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(googleToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() ) {
            throw new RuntimeException("Google token could not be verified");

        }
        Map userInfo = response.getBody();
        String email = (String) userInfo.get("email");

        return userRepository.findByEmail(email)
                .orElseThrow();
    }


}
