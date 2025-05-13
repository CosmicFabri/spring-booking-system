package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.Program;
import com.spring.spring_booking_system.repositories.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramService {
    ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public Optional<Program> findById(Long id) {
        return programRepository.findById(id);
    }

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }
}
