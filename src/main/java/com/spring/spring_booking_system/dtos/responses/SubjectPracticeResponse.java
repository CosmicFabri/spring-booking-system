package com.spring.spring_booking_system.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.spring_booking_system.dtos.PracticeDto;
import com.spring.spring_booking_system.entities.Practice;
import com.spring.spring_booking_system.entities.Subject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubjectPracticeResponse {
    private Long id;

    private String name;

    @JsonProperty("id_program")
    private Long programId;

    private List<PracticeDto> practices;

    public SubjectPracticeResponse(Subject subject, List<PracticeDto> practices) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.programId = subject.getProgram().getId();
        this.practices = practices;
    }
}
