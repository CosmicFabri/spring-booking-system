package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByDateAndSpace_Id(LocalDate date, Long spaceId);

    List<Booking> findByDateAndStartTime(LocalDate date, LocalTime startTime);
}
