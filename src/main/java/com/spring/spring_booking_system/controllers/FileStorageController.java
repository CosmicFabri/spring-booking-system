package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.repositories.FileDataRepository;
import com.spring.spring_booking_system.services.FileStorageService;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileStorageController {
    FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam Long practiceId) throws IOException {
        String uploadFile = fileStorageService.uploadFile(file, practiceId);
        return ResponseEntity.ok().body(Map.of("uploaded_file", uploadFile));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileName") String fileName) throws IOException {
        byte[] fileBytes = fileStorageService.downloadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/pdf"))
                .body(fileBytes);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileName") String fileName) throws IOException {
        String response = fileStorageService.deleteFIle(fileName);
        return ResponseEntity.ok(Map.of("message", response));
    }
}
