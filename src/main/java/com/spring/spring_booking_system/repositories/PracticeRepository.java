package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.FileData;
import com.spring.spring_booking_system.entities.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {
    List<Practice> findAllById(Long subjectId);

    List<Practice> findAllBySubjectId(Long subjectId);

    Optional<Practice> findByFile(FileData fileData);
}
