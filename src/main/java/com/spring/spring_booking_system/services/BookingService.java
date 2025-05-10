package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.dtos.BookingRequestDto;
import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.repositories.BookingRepository;
import com.spring.spring_booking_system.repositories.SpaceRepository;
import com.spring.spring_booking_system.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            SpaceRepository spaceRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
    }

    public Booking save(BookingRequestDto request) {
        Space space = spaceRepository.findById(request.getIdSpace()).orElse(null);
        User user = userRepository.findById(request.getIdUser()).orElse(null);

        Booking booking = new Booking();
        booking.setSpace(space);
        booking.setUser(user);
        booking.setDate(request.date);
        booking.setStartTime(request.startTime);
        booking.setEndTime(request.endTime);

        return bookingRepository.save(booking);
    }

    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        bookingRepository.findAll().forEach(bookings::add);

        return bookings;
    }

    public Booking findById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking update(int id, BookingRequestDto booking) {
        Booking updatedBooking = bookingRepository.findById(id).orElse(null);

        if (updatedBooking != null) {
            updatedBooking.setStartTime(booking.getStartTime());
            updatedBooking.setEndTime(booking.getEndTime());

            return bookingRepository.save(updatedBooking);
        }

        return null;
    }

    public Booking delete(int id) {
        Booking deletedBooking = bookingRepository.findById(id).orElse(null);

        if (deletedBooking != null) {
            bookingRepository.delete(deletedBooking);

            return deletedBooking;
        }

        return null;
    }

    // Checks if the booking time interval doesn't overlap with another one
    public boolean isBookingUnique(BookingRequestDto request, Optional<Booking> booking) {
        LocalDate date = request.getDate();
        Long idSpace = request.getIdSpace();
        LocalTime startTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();

        // List of bookings that match the same day and space
        List<Booking> bookings = bookingRepository.findAllByDateAndSpace_Id(date, idSpace);

        // Exclude the booking from the list of current bookings if it's being updated
        Long id = booking.map(Booking::getId).orElse(null);

        for (Booking currentBooking : bookings) {
            // Exclude booking being updated
            if (currentBooking.getId().equals(id)) {
                continue;
            }

            LocalTime currentStartTime = currentBooking.getStartTime();
            LocalTime currentEndTime = currentBooking.getEndTime();

            // If the interval overlaps, return false
            if (startTime.isBefore(currentEndTime) && endTime.isAfter(currentStartTime)) {
                return false;
            }
        }

        return true;
    }
}
