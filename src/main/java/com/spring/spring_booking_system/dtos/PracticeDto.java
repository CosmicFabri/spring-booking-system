package com.spring.spring_booking_system.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.spring_booking_system.entities.FileData;
import com.spring.spring_booking_system.entities.Practice;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeDto {
    private Long id;

    @NotBlank
    private String name;

    @JsonProperty("id_subject")
    @NotBlank
    private Long subjectId;

    @JsonProperty("file_path")
    private String filePath;

    public PracticeDto(Practice practice) {
        this.id = practice.getId();
        this.name = practice.getName();
        this.subjectId = practice.getSubject().getId();

        FileData fileData = practice.getFile();
        if(fileData != null) {
            this.filePath = fileData.getPath();
        } else {
            this.filePath = null;
        }
    }
}
