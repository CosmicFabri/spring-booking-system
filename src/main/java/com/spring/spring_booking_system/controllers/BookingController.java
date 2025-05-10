package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.BookingRequestDto;
import com.spring.spring_booking_system.dtos.BookingResponseDto;
import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        List<Booking> bookings = bookingService.findAll();
        List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingResponseDtos.add(new BookingResponseDto(booking));
        }

        return ResponseEntity.ok(bookingResponseDtos);
    }

    // getUserBookings

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable int id) {
        Booking booking = bookingService.findById(id);

        if (booking != null) {
            return ResponseEntity.ok(new BookingResponseDto(booking));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto request) {
        // Get the ID of the authenticated user
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (bookingService.isBookingUnique(request, Optional.empty())) {
            request.setIdUser(userId);
            Booking bookingCreated = bookingService.save(request);

            return ResponseEntity.ok(new BookingResponseDto(bookingCreated));
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable int id, @Valid @RequestBody BookingRequestDto request) {
        Booking booking = bookingService.findById(id);

        if (booking == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!booking.getUser().getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (bookingService.isBookingUnique(request, Optional.of(booking))) {
            bookingService.update(id, request);

            return ResponseEntity.ok(new BookingResponseDto(booking));
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<Map<String, Object>> deleteBooking(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        Booking booking = bookingService.findById(id);

        if (booking == null) {
            response.put("error", "Couldn't find a booking with such ID.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String role = "" + SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (role.equals("[ROLE_admin]")) {
            // An admin can delete a booking whenever he wants
            booking = bookingService.delete(id);

            response.put("message", "Booking deleted successfully.");
            response.put("booking", new BookingResponseDto(booking));

            return ResponseEntity.ok(response);
        } else if (role.equals("[ROLE_user]")) {
            // Get the ID of the authenticated user
            Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Get the user ID related to the booking ID of the request
            User user = bookingService.findById(id).getUser();
            Integer requestUserId = user.getId();

            if (!userId.equals(requestUserId)) {
                response.put("error", "You can't delete other's booking.");

                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            booking = bookingService.delete(id);

            response.put("message", "Booking deleted successfully.");
            response.put("booking", new BookingResponseDto(booking));

            return ResponseEntity.ok(response);
        }

        response.put("error", "Role not authorized for this operation.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
