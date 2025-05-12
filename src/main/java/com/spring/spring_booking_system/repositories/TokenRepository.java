package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    public Optional<Token> findByHash(String hash);

    List<Token> findByExpiresBefore(LocalDateTime today);
}
