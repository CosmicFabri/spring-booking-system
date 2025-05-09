package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {
}
