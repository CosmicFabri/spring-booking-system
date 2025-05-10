package com.spring.spring_booking_system.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BookingRequest {
    public Long idUser;

    @NotNull(message = "Space is mandatory.")
    public Long idSpace;

    @NotNull(message = "Day is mandatory.")
    public LocalDate day;

    @NotNull(message = "Start hour is mandatory.")
    public LocalTime startHour;

    @NotNull(message = "End hour is mandatory.")
    public LocalTime endHour;
}
