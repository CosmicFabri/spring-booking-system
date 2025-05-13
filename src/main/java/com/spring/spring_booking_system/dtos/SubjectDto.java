package com.spring.spring_booking_system.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.spring_booking_system.entities.Subject;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubjectDto {
    private Long id;

    private String name;

    @JsonProperty("id_program")
    private Long programId;

    public SubjectDto(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.programId = subject.getProgram().getId();
    }
}
