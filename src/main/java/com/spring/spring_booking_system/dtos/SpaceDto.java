package com.spring.spring_booking_system.dtos;

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
    private Map<String, LocalTime> disponibility;

    public SpaceDto(Space space) {
        this.id = space.getId();
        this.name = space.getName();
        this.description = space.getDescription();
        this.capacity = space.getCapacity();
        this.disponibility = Map.of("start",space.getDisponibilityStart(),"end",space.getDisponibilityEnd());
    }
}
