package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.Program;
import com.spring.spring_booking_system.entities.Subject;
import com.spring.spring_booking_system.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findById(Long id) {
        return subjectRepository.findById(id);
    }

    public List<Subject> findByProgram(Long programId) {
        return subjectRepository.findByProgramId(programId);
    }
}
