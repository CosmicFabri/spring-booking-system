package com.spring.spring_booking_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "practices")
public class Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_subject", referencedColumnName = "id")
    // Can be null
    private Subject subject;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "file", referencedColumnName = "id")
    // Can be null
    private FileData file;

    @CreationTimestamp
    @Column(updatable = false, name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @UpdateTimestamp
    @Column(updatable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
