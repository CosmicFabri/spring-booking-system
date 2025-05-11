package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.requests.BookingRequest;
import com.spring.spring_booking_system.dtos.responses.BookingResponse;
import com.spring.spring_booking_system.entities.Booking;
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
            SpaceService spaceService) {
        this.bookingService = bookingService;
        this.spaceService = spaceService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<Booking> bookings = bookingService.findAll();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingResponses.add(new BookingResponse(booking));
        }

        return ResponseEntity.ok(bookingResponses);
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
        List<BookingResponse> bookingResponseDtos = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingResponseDtos.add(new BookingResponse(booking));
        }

        response.put("message", "Bookings retrieved correctly.");
        response.put("bookings", bookingResponseDtos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable int id) {
        Booking booking = bookingService.findById(id);

        if (booking == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // USER SCOPE
        String role = "" + SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (role.equals("[ROLE_user]")) {
            Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Get the user ID related to the booking ID of the request
            User user = bookingService.findById(id).getUser();
            Long requestUserId = user.getId();

            if (!userId.equals(requestUserId)) {
                //response.put("error", "You can't retrieve other's booking.");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        // ADMIN SCOPE
        return ResponseEntity.ok(new BookingResponse(booking));
    }

    @GetMapping("/user/pending")
    public ResponseEntity<List<BookingResponse>> getPendingBookings() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Booking> bookings = bookingService.getPendingUserBookings(userId);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingResponses.add(new BookingResponse(booking));
        }

        return ResponseEntity.ok(bookingResponses);
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

        // Error: there has been a conflict with the requested booking interval
        //response.put("error", "Can't create the booking - the time interval overlaps with another one.");

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable int id, @Valid @RequestBody BookingRequest request) {
        // Get the ID of the authenticated user
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Booking booking = bookingService.findById(id);

        // Error: no booking with such ID
        if (booking == null) {
            //response.put("error", "No such booking exists.");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Error: user ID doesn't match
        if (!booking.getUser().getId().equals(userId)) {
            //response.put("error", "Booking user ID doesn't match.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Error: time interval conflict
        if (!bookingService.isBookingUnique(request, Optional.of(booking))) {
            //response.put("error", "There has been a time interval conflict.");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        bookingService.update(id, request);

        return ResponseEntity.ok(new BookingResponse(booking));
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
            response.put("booking", new BookingResponse(booking));

            return ResponseEntity.ok(response);
        } else if (role.equals("[ROLE_user]")) {
            Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Get the user ID related to the booking ID of the request
            User user = bookingService.findById(id).getUser();
            Long requestUserId = user.getId();

            if (!userId.equals(requestUserId)) {
                response.put("error", "You can't delete other's booking.");

                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            booking = bookingService.delete(id);

            response.put("message", "Booking deleted successfully.");
            response.put("booking", new BookingResponse(booking));

            return ResponseEntity.ok(response);
        }

        response.put("error", "Role not authorized for this operation.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
