package com.spring.spring_booking_system.dtos;

import com.spring.spring_booking_system.entities.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class BookingResponseDto {
    public Long id;
    public Long spaceId;
    public Integer userId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public Date createdAt;
    public Date updatedAt;

    public BookingResponseDto(Booking booking) {
        this.id = booking.getId();
        this.spaceId = booking.getSpace().getId();
        this.userId = booking.getUser().getId();
        this.date = booking.getDate();
        this.startTime = booking.getStartTime();
        this.endTime = booking.getEndTime();
        this.createdAt = booking.getCreatedAt();
        this.updatedAt = booking.getUpdatedAt();
    }
}
