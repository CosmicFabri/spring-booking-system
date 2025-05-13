package com.spring.spring_booking_system.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String type;

    @Column
    private String path;

    @CreationTimestamp
    @Column(updatable = false, name = "uploaded_at")
    private LocalDateTime createdAt;
}
