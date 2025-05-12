package com.spring.spring_booking_system.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id_space")
    public Long idSpace;

    @NotNull(message = "Day is mandatory.")
    public LocalDate day;

    @NotNull(message = "Start hour is mandatory.")
    @JsonProperty("start_hour")
    public LocalTime startHour;

    @NotNull(message = "End hour is mandatory.")
    @JsonProperty("end_hour")
    public LocalTime endHour;
}
