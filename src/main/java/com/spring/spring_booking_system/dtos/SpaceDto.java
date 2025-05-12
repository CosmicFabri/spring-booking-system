package com.spring.spring_booking_system.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.spring_booking_system.entities.Space;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SpaceDto {
    private Long id;
    private String name;
    private String description;
    private int capacity;
    private Disponibility disponibility;

    public SpaceDto(Space space) {
        this.id = space.getId();
        this.name = space.getName();
        this.description = space.getDescription();
        this.capacity = space.getCapacity();
        this.disponibility = new Disponibility(space);
    }
}

@Getter
@Setter
class Disponibility {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime end;
    public Disponibility(Space space) {
        this.start = space.getDisponibilityStart();
        this.end = space.getDisponibilityEnd();
    }
}
