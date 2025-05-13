package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.ProgramDto;
import com.spring.spring_booking_system.entities.Program;
import com.spring.spring_booking_system.services.ProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/program")
public class ProgramController {
    ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping
    public ResponseEntity<List<ProgramDto>> getAllPrograms() {
        List<Program> programs = programService.getAllPrograms();
        List<ProgramDto> programsDto = new ArrayList<>();
        for (Program program : programs) {
            programsDto.add(new ProgramDto(program));
        }
        return ResponseEntity.ok(programsDto);
    }
}
