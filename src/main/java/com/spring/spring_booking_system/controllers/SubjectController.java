package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.PracticeDto;
import com.spring.spring_booking_system.dtos.SubjectDto;
import com.spring.spring_booking_system.dtos.responses.SubjectPracticeResponse;
import com.spring.spring_booking_system.entities.Practice;
import com.spring.spring_booking_system.entities.Program;
import com.spring.spring_booking_system.entities.Subject;
import com.spring.spring_booking_system.repositories.SubjectRepository;
import com.spring.spring_booking_system.services.PracticeService;
import com.spring.spring_booking_system.services.ProgramService;
import com.spring.spring_booking_system.services.SubjectService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.preauth.x509.SubjectDnX509PrincipalExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    private final ProgramService programService;
    private final PracticeService practiceService;

    public SubjectController(SubjectService subjectService, ProgramService programService, PracticeService practiceService) {
        this.subjectService = subjectService;
        this.programService = programService;
        this.practiceService = practiceService;
    }

    @GetMapping
    public ResponseEntity<?> getAllSubjects(@RequestParam(required = false) Long programId) {
        List<Subject> subjects;

        if (programId != null) {
            subjects = subjectService.findByProgram(programId);
            List<SubjectPracticeResponse> subjectDtos = new ArrayList<>();

            for (Subject subject : subjects) {
                List<Practice> practices = practiceService.getBySubject(subject.getId());
                List<PracticeDto> practiceDtos = new ArrayList<>();
                for (Practice practice : practices) {
                    practiceDtos.add(new PracticeDto(practice));
                }
                subjectDtos.add(new SubjectPracticeResponse(subject, practiceDtos));
            }

            return ResponseEntity.ok(subjectDtos);
        } else {
            subjects = subjectService.findAll();
            List<SubjectDto> subjectDtos = new ArrayList<>();

            for (Subject subject : subjects) {
                subjectDtos.add(new SubjectDto(subject));
            }

            return ResponseEntity.ok(subjectDtos);
        }


    }
}
