package com.spring.spring_booking_system.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@Table(name = "roles")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToMany
    @JoinColumn(name = "idRole", referencedColumnName = "id")
    private List<User> users;

    @Column(nullable = false)
    private String description;
}
