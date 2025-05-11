package com.spring.spring_booking_system.dtos.responses;

import com.spring.spring_booking_system.entities.Booking;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class BookingResponse {
    public Long id;
    public Long spaceId;
    public Long userId;
    public LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    public LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    public LocalTime endTime;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.spaceId = booking.getSpace().getId();
        this.userId = booking.getUser().getId();
        this.date = booking.getDay();
        this.startTime = booking.getStartHour();
        this.endTime = booking.getEndHour();
        this.createdAt = booking.getCreatedAt();
        this.updatedAt = booking.getUpdatedAt();
    }
}
