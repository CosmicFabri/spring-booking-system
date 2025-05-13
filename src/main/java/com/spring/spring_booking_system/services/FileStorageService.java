package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.FileData;
import com.spring.spring_booking_system.repositories.FileDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class FileStorageService {
    private final FileDataRepository fileDataRepository;

    @Value("${file.upload-dir}")
    private String FOLDER_PATH;

    public FileStorageService(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .path(filePath)
                .build()
        );

        file.transferTo(new File(filePath));

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
