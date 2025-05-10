package com.spring.spring_booking_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "spaces")
@Entity
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Name is mandatory.")
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Capacity is mandatory.")
    private int capacity;

    @Column(columnDefinition = "TIME", nullable = false)
    @NotNull(message = "Disponibility start is mandatory.")
    private LocalTime disponibilityStart;

    @Column(columnDefinition = "TIME", nullable = false)
    @NotNull(message = "Disponibility end is mandatory.")
    private LocalTime disponibilityEnd;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
