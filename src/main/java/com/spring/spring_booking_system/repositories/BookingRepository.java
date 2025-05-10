package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByDayAndSpace_Id(LocalDate day, Long spaceId);
}
