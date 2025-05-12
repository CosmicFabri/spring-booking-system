package com.spring.spring_booking_system.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.services.BookingService;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class BookingDto {
    private Long id;

    private LocalDate day;

    @JsonProperty("space_id")
    private Long spaceId;

    @JsonProperty("space_name")
    private String spaceName;

    @JsonProperty("id_user")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("start_hour")
    private LocalTime startHour;

    @JsonProperty("end_hour")
    private LocalTime endHour;

    private boolean editable;

    public BookingDto(Booking booking) {
        this.id = booking.getId();
        this.day = booking.getDay();
        this.spaceId = booking.getSpace().getId();
        this.spaceName = booking.getSpace().getName();
        this.userId = booking.getUser().getId();
        this.userName = booking.getUser().getFullName();
        this.startHour = booking.getStartHour();
        this.endHour = booking.getEndHour();
        this.editable = BookingService.isEditable(booking);
    }

}
