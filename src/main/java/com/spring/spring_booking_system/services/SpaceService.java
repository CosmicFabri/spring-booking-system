package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.repositories.SpaceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpaceService {
    private final SpaceRepository spaceRepository;

    SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    public List<Space> getAllSpaces() {
        List<Space> spaces = new ArrayList<>();
        spaceRepository.findAll().forEach(spaces::add);

        return spaces;
    }

    public Space getSpaceById(int id) {
        return spaceRepository.findById(id).orElse(null);
    }

    public Space addSpace(Space space) {
        return spaceRepository.save(space);
    }

    public Space updateSpace(int id, Space space) {
        Space updatedSpace = spaceRepository.findById(id).orElse(null);

        if (updatedSpace != null) {
            updatedSpace.setName(space.getName());
            updatedSpace.setDescription(space.getDescription());
            updatedSpace.setDisponibilityStart(space.getDisponibilityStart());
            updatedSpace.setDisponibilityEnd(space.getDisponibilityEnd());

            return spaceRepository.save(updatedSpace);
        }

        return null;
    }

    public Space deleteSpace(int id) {
        Space deletedSpace = spaceRepository.findById(id).orElse(null);

        if (deletedSpace != null) {
            spaceRepository.delete(deletedSpace);

            return deletedSpace;
        }

        return null;
    }
}
