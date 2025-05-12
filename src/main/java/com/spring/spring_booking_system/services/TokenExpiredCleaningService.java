package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.Token;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenExpiredCleaningService {
    TokenService tokenService;
    public TokenExpiredCleaningService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanExpiredTokens() {
        System.out.println("Cleaning expired tokens");

        List<Token> tokens = tokenService.findExpiredTokens();
        for (Token token : tokens) {
            tokenService.delete(token);
        }
    }
}
