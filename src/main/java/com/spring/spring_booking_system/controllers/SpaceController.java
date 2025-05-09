package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.repositories.SpaceRepository;
import com.spring.spring_booking_system.services.SpaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/spaces")
public class SpaceController {
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping
    public ResponseEntity<List<Space>> getSpaces() {
        List<Space> spaces = spaceService.getAllSpaces();

        return new ResponseEntity<>(spaces, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Space> getSpaceById(@PathVariable int id) {
        Space space = spaceService.getSpaceById(id);

        if (space != null) {
            return new ResponseEntity<>(space, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Space> addSpace(@Valid @RequestBody Space space) {
        Space newSpace = spaceService.addSpace(space);

        return new ResponseEntity<>(newSpace, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Space> updateSpace(@PathVariable int id, @Valid @RequestBody Space space) {
        Space updatedSpace = spaceService.updateSpace(id, space);

        if (updatedSpace != null) {
            return new ResponseEntity<>(updatedSpace, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Space> deleteSpace(@PathVariable int id) {
        Space space = spaceService.deleteSpace(id);

        if (space != null) {
            return new ResponseEntity<>(space, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
