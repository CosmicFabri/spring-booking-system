package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.Space;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends CrudRepository<Space, Integer> {
}
