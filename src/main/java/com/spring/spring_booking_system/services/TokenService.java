package com.spring.spring_booking_system.services;
/* TOKENS STORED IN blacklist table */

import com.spring.spring_booking_system.entities.Token;
import com.spring.spring_booking_system.repositories.TokenRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public TokenService(TokenRepository tokenRepository, JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }

    public Token revokeToken(String jwt) {
        Token token = new Token();
        LocalDateTime expirationDate = jwtService.extractExpiration(jwt).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();;

        token.setExpires(expirationDate);
        token.setHash(sha256(jwt));

        return tokenRepository.save(token);
    }

    public Optional<Token> findByHash(String hash) {
        return tokenRepository.findByHash(hash);
    }

    public boolean isTokenRevoked(String jwt) {
        Optional<Token> token = findByHash(sha256(jwt));
        return token.isPresent();
    }

    public Token delete(String jwt) {

        Token token = tokenRepository.findByHash(sha256(jwt)).orElse(null);
        if(token != null) {
            tokenRepository.delete(token);
        }

        return null;
    }

    public Token delete(Token token) {
        tokenRepository.delete(token);
        return token;
    }

    public List<Token> findExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        return tokenRepository.findByExpiresBefore(now);
    }

    public String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());

            // Convierte a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error al calcular hash SHA-256", e);
        }
    }
}
