package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.PracticeDto;
import com.spring.spring_booking_system.entities.Practice;
import com.spring.spring_booking_system.services.PracticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/practice")
public class PracticeController {
    private PracticeService practiceService;
    public PracticeController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }

    @GetMapping
    public ResponseEntity<List<PracticeDto>> getPractices(@RequestParam(required = false) Long subjectId) {
        List<Practice> practices;
        if (subjectId != null) {
            practices = practiceService.getBySubject(subjectId);
        } else {
            practices = practiceService.getAllPractices();
        }
        List<PracticeDto> practiceDtos = new ArrayList<>();
        for (Practice practice : practices) {
            practiceDtos.add(new PracticeDto(practice));
        }

        return ResponseEntity.ok(practiceDtos);
    }
}
