package com.spring.spring_booking_system.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BookingRequestDto {
    @NotNull(message = "Space is mandatory.")
    public Integer idSpace;

    @NotNull(message = "User is mandatory.")
    public Integer idUser;

    @NotNull(message = "Date is mandatory.")
    public LocalDate date;

    @NotNull(message = "Start time is mandatory.")
    public LocalTime startTime;

    @NotNull(message = "End time is mandatory.")
    public LocalTime endTime;
}
