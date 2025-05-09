package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.repositories.SpaceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/spaces")
public class SpaceController {
    private final SpaceRepository spaceRepository;

    public SpaceController(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<Space>> getSpaces() {
        List<Space> spaces = new ArrayList<>();
        spaceRepository.findAll().forEach(spaces::add);

        return new ResponseEntity<>(spaces, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Space> getSpaceById(@PathVariable int id) {
        Space space = spaceRepository.findById(id).orElse(null);

        if (space != null) {
            return new ResponseEntity<>(space, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Space> addSpace(@Valid @RequestBody Space space) {
        Space newSpace = spaceRepository.save(space);
        return new ResponseEntity<>(newSpace, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Space> updateSpace(@PathVariable int id, @Valid @RequestBody Space space) {
        Space updatedSpace = spaceRepository.findById(id).orElse(null);

        if (updatedSpace != null) {
            updatedSpace.setName(space.getName());
            updatedSpace.setDescription(space.getDescription());
            updatedSpace.setCapacity(space.getCapacity());
            updatedSpace.setDisponibilityStart(space.getDisponibilityStart());
            updatedSpace.setDisponibilityEnd(space.getDisponibilityEnd());

            return ResponseEntity.ok(spaceRepository.save(updatedSpace));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Space> deleteSpace(@PathVariable int id) {
        Space space = spaceRepository.findById(id).orElse(null);

        if (space != null) {
            spaceRepository.delete(space);
            return new ResponseEntity<>(space, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
