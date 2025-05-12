package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.dtos.requests.BookingRequest;
import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.exceptions.UserNotFoundException;
import com.spring.spring_booking_system.repositories.BookingRepository;
import com.spring.spring_booking_system.repositories.SpaceRepository;
import com.spring.spring_booking_system.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public BookingService(
            BookingRepository bookingRepository,
            SpaceRepository spaceRepository,
            UserRepository userRepository,
            EmailService emailService
    ) {
        this.bookingRepository = bookingRepository;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Booking save(BookingRequest request) {
        Space space = spaceRepository.findById(request.getIdSpace()).orElse(null);
        User user = userRepository.findById(request.getIdUser()).orElseThrow(() -> new UserNotFoundException(request.getIdUser()));

        Booking booking = new Booking();
        booking.setSpace(space);
        booking.setUser(user);
        booking.setDay(request.day);
        booking.setStartHour(request.startHour);
        booking.setEndHour(request.endHour);

        // Send the confirmation email
        emailService.sendConfirmationEmail(user, booking, space.getName());

        return bookingRepository.save(booking);
    }

    public List<Booking> findAll() {
        return new ArrayList<>(bookingRepository.findAll());
    }

    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    public Page<Booking> findAllByUserId(Long id, Pageable pageable) {

        return bookingRepository.findAllByUser_Id(id, pageable);
    }


    public List<Booking> findFiltered(LocalDate day) {
        return bookingRepository.findAllByDay(day);
    }

    public Booking findById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking update(int id, BookingRequest booking) {
        Booking updatedBooking = bookingRepository.findById(id).orElse(null);


        if (updatedBooking != null) {
            Space space = spaceRepository.findById(booking.getIdSpace()).orElse(updatedBooking.getSpace());

            updatedBooking.setSpace(space);
            updatedBooking.setDay(booking.getDay());
            updatedBooking.setStartHour(booking.getStartHour());
            updatedBooking.setEndHour(booking.getEndHour());

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

    public List<Booking> getPendingUserBookings(Long userId) {
        return bookingRepository.findByUserPending(LocalDate.now(), LocalTime.now(), userId);
    }

    public List<Booking> getScheduledBookings(Long spaceId, LocalDate day, Long bookingId) {
        if(bookingId == null) {
            return bookingRepository.findAllByDayAndSpace_Id(day, spaceId);
        }
        else {
            return bookingRepository.findAllByDayAndSpace_IdAndIdNot(day, spaceId, bookingId);
        }

    }

    // Checks if the booking time interval doesn't overlap with another one
    public boolean isBookingUnique(BookingRequest request, Optional<Booking> booking) {
        LocalDate date = request.getDay();
        Long idSpace = request.getIdSpace();
        LocalTime startTime = request.getStartHour();
        LocalTime endTime = request.getEndHour();

        // List of bookings that match the same day and space
        List<Booking> bookings = bookingRepository.findAllByDayAndSpace_Id(date, idSpace);

        // Exclude the booking from the list of current bookings if it's being updated
        Long id = booking.map(Booking::getId).orElse(null);

        for (Booking currentBooking : bookings) {
            // Exclude booking being updated
            if (currentBooking.getId().equals(id)) {
                continue;
            }

            LocalTime currentStartTime = currentBooking.getStartHour();
            LocalTime currentEndTime = currentBooking.getEndHour();

            // If the interval overlaps, return false
            if (startTime.isBefore(currentEndTime) && endTime.isAfter(currentStartTime)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isEditable(Booking booking) {
        LocalDateTime targetTime = LocalDateTime.now().plusDays(1);
        LocalDateTime bookingTime = LocalDateTime.of(booking.getDay(), booking.getStartHour());

        return targetTime.isBefore(bookingTime);
    }
}
