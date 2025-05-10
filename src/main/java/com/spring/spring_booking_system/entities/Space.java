package com.spring.spring_booking_system.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(nullable = false)
    @NotNull(message = "Disponibility start is mandatory.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime disponibilityStart;

    @Column(nullable = false)
    @NotNull(message = "Disponibility end is mandatory.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime disponibilityEnd;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @AssertTrue(message = "disponibilityEnd must be after disponibilityStart")
    @JsonIgnore
    public boolean isDisponibilityValid() {
        return disponibilityEnd.isAfter(disponibilityStart);
    }
}
