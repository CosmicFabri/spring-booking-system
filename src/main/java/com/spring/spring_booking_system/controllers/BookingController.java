package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.BookingRequestDto;
import com.spring.spring_booking_system.dtos.BookingResponseDto;
import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.services.BookingService;
import com.spring.spring_booking_system.services.SpaceService;
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
    private final SpaceService spaceService;

    Map<String, Object> response = new HashMap<>();

    public BookingController(
            BookingService bookingService,
            SpaceService spaceService
    ) {
        this.bookingService = bookingService;
        this.spaceService = spaceService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        List<Booking> bookings = bookingService.findAll();
        List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingResponseDtos.add(new BookingResponseDto(booking));
        }

        return ResponseEntity.ok(bookingResponseDtos);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Map<String, Object>> getUserBookings() {
        // Get the ID of the authenticated user
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Error: no user authenticated
        if (userId == null) {
            response.put("error", "No user authenticated.");
        }

        // Get all the bookings by userId
        List<Booking> bookings = bookingService.findAllByUserId(userId);
        List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingResponseDtos.add(new BookingResponseDto(booking));
        }

        response.put("message", "Bookings retrieved correctly.");
        response.put("bookings", bookingResponseDtos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Map<String, Object>> getBookingById(@PathVariable int id) {
        Booking booking = bookingService.findById(id);

        // Error: no booking with such ID
        if (booking == null) {
            response.put("error", "Couldn't find a booking with such ID.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("message", "Booking retrieved correctly.");
        response.put("booking", new BookingResponseDto(booking));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Map<String, Object>> createBooking(@Valid @RequestBody BookingRequestDto request) {
        // Get the space base off of the ID of the request
        Space space = spaceService.getSpaceById(request.getIdSpace());

        // Error: no space with such ID
        if (space == null) {
            response.put("error", "Couldn't find a space with such ID.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Get the ID of the authenticated user
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (bookingService.isBookingUnique(request, Optional.empty())) {
            request.setIdUser(userId);
            Booking bookingCreated = bookingService.save(request);

            response.put("message", "Booking created successfully.");
            response.put("booking", new BookingResponseDto(bookingCreated));

            return ResponseEntity.ok(response);
        }

        // Error: there has been a conflict with the requested booking interval
        response.put("error", "Can't create the booking - the time interval overlaps with another one.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Map<String, Object>> updateBooking(@PathVariable int id, @Valid @RequestBody BookingRequestDto request) {
        Booking booking = bookingService.findById(id);

        // Error: no booking with such ID
        if (booking == null) {
            response.put("error", "No such booking exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Get the ID of the authenticated user
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Error: user ID doesn't match
        if (!booking.getUser().getId().equals(userId)) {
            response.put("error", "Booking user ID doesn't match.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        request.setIdUser(userId);

        // Error: time interval conflict
        if (!bookingService.isBookingUnique(request, Optional.of(booking))) {
            response.put("error", "There has been a time interval conflict.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        bookingService.update(id, request);

        response.put("message", "Booking updated successfully.");
        response.put("booking", request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public ResponseEntity<Map<String, Object>> deleteBooking(@PathVariable int id) {
        Booking booking = bookingService.findById(id);

        if (booking == null) {
            response.put("error", "Couldn't find a booking with such ID.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String role = "" + SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        // Only an admin can delete a booking whenever they want
        if (role.equals("[ROLE_admin]")) {
            booking = bookingService.delete(id);

            response.put("message", "Booking deleted successfully.");
            response.put("booking", new BookingResponseDto(booking));

            return ResponseEntity.ok(response);
        } else if (role.equals("[ROLE_user]")) {
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
