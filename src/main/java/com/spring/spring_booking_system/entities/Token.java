package com.spring.spring_booking_system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name= "token_blacklist")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hash;

    @Column
    private LocalDateTime expires;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime revoked;
}
