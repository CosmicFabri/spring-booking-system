package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.FileData;
import com.spring.spring_booking_system.repositories.FileDataRepository;
import org.springframework.stereotype.Service;

@Service
public class FileDataService {
    FileDataRepository fileDataRepository;

    public FileDataService(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }

    FileData save(FileData fileData) {
        return fileDataRepository.save(fileData);
    }

    FileData delete(Long fileDataId) {
        FileData fileData = fileDataRepository.findById(fileDataId).orElse(null);

        if (fileData != null) {
            fileDataRepository.delete(fileData);
            return fileData;
        }

        return null;
    }
}
