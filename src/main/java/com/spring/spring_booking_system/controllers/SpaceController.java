package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.repositories.SpaceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/spaces")
public class SpaceController {
    private final SpaceRepository spaceRepository;

    public SpaceController(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Space> addSpace(@Valid @RequestBody Space space) {
        Space newSpace = spaceRepository.save(space);
        return new ResponseEntity<>(newSpace, HttpStatus.CREATED);
    }
}
