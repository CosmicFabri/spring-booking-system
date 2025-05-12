package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.dtos.requests.SpaceRequest;
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

    public Space getSpaceById(Long id) {
        return spaceRepository.findById(id).orElse(null);
    }

    public Space addSpace(SpaceRequest spaceRequest) {
        Space newSpace = new Space();
        newSpace.setName(spaceRequest.getName());
        newSpace.setCapacity(spaceRequest.getCapacity());
        newSpace.setDescription(spaceRequest.getDescription());
        newSpace.setDisponibilityStart(spaceRequest.getDisponibilityStart());
        newSpace.setDisponibilityEnd(spaceRequest.getDisponibilityEnd());

        return spaceRepository.save(newSpace);
    }

    public Space updateSpace(Long id, Space space) {
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

    public Space deleteSpace(Long id) {
        Space deletedSpace = spaceRepository.findById(id).orElse(null);

        if (deletedSpace != null) {
            spaceRepository.delete(deletedSpace);

            return deletedSpace;
        }

        return null;
    }
}
