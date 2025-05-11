package com.spring.spring_booking_system.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@Table(name = "bookings")
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "idSpace", referencedColumnName = "id")
    private Space space;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @NotNull
    @Column(name = "day", nullable = false)
    private LocalDate day;

    @NotNull
    @Column(columnDefinition = "TIME")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startHour;

    @NotNull
    @Column(columnDefinition = "TIME")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endHour;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
