package com.spring.spring_booking_system.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Map;

@Setter
@Getter
public class SpaceRequest {
    @NotNull(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Capacity is mandatory")
    private int capacity;

    @NotNull(message = "Disponibility Start is mandatory")
    @JsonProperty("disponibility_start")
    private LocalTime disponibilityStart;

    @NotNull(message = "Disponibility End is mandatory")
    @JsonProperty("disponibility_end")
    private LocalTime disponibilityEnd;

}
