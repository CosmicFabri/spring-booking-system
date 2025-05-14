package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.FileData;
import com.spring.spring_booking_system.entities.Practice;
import com.spring.spring_booking_system.repositories.FileDataRepository;
import com.spring.spring_booking_system.repositories.PracticeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class FileStorageService {
    private final FileDataRepository fileDataRepository;
    private final PracticeService practiceService;
    private final PracticeRepository practiceRepository;

    @Value("${file.upload-dir}")
    private String FOLDER_PATH;

    public FileStorageService(FileDataRepository fileDataRepository, PracticeService practiceService, PracticeRepository practiceRepository) {
        this.fileDataRepository = fileDataRepository;
        this.practiceService = practiceService;
        this.practiceRepository = practiceRepository;
    }

    public String deleteFIle(String fileName) {
        // TODO: Include Exceptions

        FileData fileData = fileDataRepository.findByName(fileName)
                .orElseThrow(() -> new RuntimeException("File not found in DB"));

        Practice practice = practiceRepository.findByFile(fileData)
                .orElseThrow(() -> new RuntimeException("File not found in DB"));

        File file = new File(fileData.getPath());
        try {
            file.delete();
        } catch (Exception e) {
            // Throw Exception
        }

        practice.setFile(null);
        practiceRepository.save(practice);
        return fileName + " Deleted";
    }

    public String uploadFile(MultipartFile file, Long practiceId) throws IOException {
        Practice practice = practiceService.findByPracticeId(practiceId).orElse(null);
        if (practice == null) {
            throw new IOException("Practice not found"); // Temporal exception until I made the good one
        }

        String name = practiceId.toString() + practice.getName().replaceAll("\\s+", "");
        String filePath = FOLDER_PATH + name;

        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(name)
                .type(file.getContentType())
                .path(filePath)
                .build()
        );

        file.transferTo(new File(filePath));
        practice.setFile(fileData);
        practiceRepository.save(practice);

        if(fileData.getId() != null) {
            return "File uploaded successfully";
        }

        return null;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        Optional<FileData> dbFileData = fileDataRepository.findByName(fileName);
        String filePath = dbFileData.get().getPath();

        byte[] fileContent = Files.readAllBytes(new File(filePath).toPath());

        return fileContent;
    }
}
