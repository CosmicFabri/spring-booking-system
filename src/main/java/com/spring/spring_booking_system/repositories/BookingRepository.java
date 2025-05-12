package com.spring.spring_booking_system.repositories;

import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.entities.Space;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByDay(LocalDate day);

    List<Booking> findAllByDayAndSpace_Id(LocalDate day, Long spaceId);

    List<Booking> findAllByDayAndSpace_IdAndIdNot(LocalDate day, Long spaceId, Long id);

    List<Booking> findByDayAndStartHour(LocalDate date, LocalTime startHour);

    Page<Booking> findAllByUser_Id(Long userId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND (b.day > :day OR (b.day = :day AND b.startHour > :startHour)) ORDER BY b.day ASC")
    List<Booking> findByUserPending(LocalDate day, LocalTime startHour, Long userId);
}
