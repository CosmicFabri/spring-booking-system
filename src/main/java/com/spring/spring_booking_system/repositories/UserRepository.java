package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
