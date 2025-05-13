package com.spring.spring_booking_system.dtos;

import com.spring.spring_booking_system.entities.Program;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProgramDto {
    private Long id;
    private String name;

    public ProgramDto(Program program) {
        this.id = program.getId();
        this.name = program.getName();
    }
}
