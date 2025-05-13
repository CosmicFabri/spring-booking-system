package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.dtos.PracticeDto;
import com.spring.spring_booking_system.entities.Practice;
import com.spring.spring_booking_system.repositories.PracticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PracticeService {
    PracticeRepository practiceRepository;

    public PracticeService(PracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    public List<Practice> getAllPractices() {
        return practiceRepository.findAll();
    }

    public List<Practice> getBySubject(Long subjectId) {
        return practiceRepository.findAllBySubjectId(subjectId);
    }

}
