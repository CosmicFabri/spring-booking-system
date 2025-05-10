package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.requests.BookingRequest;
import com.spring.spring_booking_system.dtos.responses.BookingResponse;
import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.repositories.UserRepository;
import com.spring.spring_booking_system.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(
            BookingService bookingService,
            UserRepository userRepository
    ) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<Booking> bookings = bookingService.findAll();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingResponses.add(new BookingResponse(booking));
        }

        return ResponseEntity.ok(bookingResponses);
    }

    // getUserBookings

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable int id) {
        Booking booking = bookingService.findById(id);

        if (booking != null) {
            return ResponseEntity.ok(new BookingResponse(booking));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        // Get the ID of the authenticated user
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (bookingService.isBookingUnique(request, Optional.empty())) {
            request.setIdUser(userId);
            Booking bookingCreated = bookingService.save(request);

            return ResponseEntity.ok(new BookingResponse(bookingCreated));
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable int id, @Valid @RequestBody BookingRequest request) {
        // Get the ID of the authenticated user
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Booking booking = bookingService.findById(id);

        if (!booking.getUser().getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (booking != null) {
            if (bookingService.isBookingUnique(request, Optional.of(booking))) {
                bookingService.update(id, request);

                return ResponseEntity.ok(new BookingResponse(booking));
            }
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<BookingResponse> deleteBooking(@PathVariable int id) {
        Booking booking = bookingService.delete(id);

        if (booking != null) {
            return ResponseEntity.ok(new BookingResponse(booking));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
