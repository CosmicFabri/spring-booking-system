package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.SpaceDto;
import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.services.SpaceService;
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
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping
    public ResponseEntity<List<SpaceDto>> getSpaces() {
        List<Space> spaces = spaceService.getAllSpaces();
        List<SpaceDto> spacesDto = new ArrayList<>();
        for (Space space : spaces) {
            spacesDto.add(new SpaceDto(space));
        }
        return new ResponseEntity<>(spacesDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceDto> getSpaceById(@PathVariable Long id) {
        Space space = spaceService.getSpaceById(id);

        if (space != null) {
            return new ResponseEntity<>(new SpaceDto(space), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SpaceDto> addSpace(@Valid @RequestBody Space space) {
        Space newSpace = spaceService.addSpace(space);

        return new ResponseEntity<>(new SpaceDto(newSpace), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SpaceDto> updateSpace(@PathVariable Long id, @Valid @RequestBody Space space) {
        Space updatedSpace = spaceService.updateSpace(id, space);

        if (updatedSpace != null) {
            return new ResponseEntity<>(new SpaceDto(updatedSpace), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SpaceDto> deleteSpace(@PathVariable Long id) {
        Space space = spaceService.deleteSpace(id);

        if (space != null) {
            return new ResponseEntity<>(new SpaceDto(space), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
