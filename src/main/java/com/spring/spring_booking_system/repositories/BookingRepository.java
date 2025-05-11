package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByDayAndSpace_Id(LocalDate day, Long spaceId);

    List<Booking> findByDayAndStartHour(LocalDate date, LocalTime startHour);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.day > :day OR (b.day = :day AND b.startHour > :startHour)")
    List<Booking> findByUserPending(LocalDate day, LocalTime startHour, Long userId);

}
