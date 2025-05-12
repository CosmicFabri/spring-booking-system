package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.dtos.BookingDto;
import com.spring.spring_booking_system.dtos.requests.BookingRequest;
import com.spring.spring_booking_system.dtos.responses.BookingResponse;
import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.services.BookingService;
import com.spring.spring_booking_system.services.SpaceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
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

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public Page<BookingDto> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Booking> bookingsPage = bookingService.findAll(pageable);

        // Map each Booking to a BookingResponse
        return bookingsPage.map(BookingDto::new);
    }

    @GetMapping("/day/{day}")
    public ResponseEntity<List<BookingDto>> addBooking(@PathVariable LocalDate day) {
        List<Booking> bookings = bookingService.findFiltered(day);
        List<BookingDto> bookingsDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingsDto.add(new BookingDto(booking));
        }

        return ResponseEntity.ok(bookingsDto);
    }

    // getUserBookings
    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    public Page<BookingDto> getUserBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending
    ) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Booking> bookingsPage = bookingService.findAllByUserId(userId, pageable);

        // Map each Booking to a BookingResponse
        return bookingsPage.map(BookingDto::new);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable int id) {
        Booking booking = bookingService.findById(id);

        if (booking == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // USER SCOPE
        String role = "" + SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (role.equals("[ROLE_user]")) {
            Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Get the user ID related to the booking ID of the request
            Long requestUserId = bookingService.findById(id).getUser().getId();

            if (!userId.equals(requestUserId)) {
                //response.put("error", "You can't retrieve other's booking.");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        // ADMIN SCOPE
        return ResponseEntity.ok(new BookingDto(booking));
    }

    @GetMapping("/hours")
    public ResponseEntity<List<BookingDto>> getScheduledBookings(
            @RequestParam Long idSpace,
            @RequestParam LocalDate day,
            @RequestParam(required = false) Long idBooking ) {

        List<Booking> bookings = bookingService.getScheduledBookings(idSpace, day, idBooking);
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDtos.add(new BookingDto(booking));
        }

        return ResponseEntity.ok(bookingDtos);
    }


    @GetMapping("/user/pending")
    public ResponseEntity<List<BookingDto>> getPendingBookings() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Booking> bookings = bookingService.getPendingUserBookings(userId);
        List<BookingDto> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingResponses.add(new BookingDto(booking));
        }

        return ResponseEntity.ok(bookingResponses);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingRequest request) {
        // Get the ID of the authenticated user
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (bookingService.isBookingUnique(request, Optional.empty())) {
            request.setIdUser(userId);
            Booking bookingCreated = bookingService.save(request);

            return ResponseEntity.ok(new BookingDto(bookingCreated));
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
        }

        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the user ID related to the booking ID of the request
        Long requestUserId = bookingService.findById(id).getUser().getId();

        if (!userId.equals(requestUserId)) {
            response.put("error", "User ID does not match.");

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        booking = bookingService.delete(id);

        response.put("message", "Booking deleted successfully.");
        response.put("booking", new BookingResponse(booking));

        return ResponseEntity.ok(response);
    }
}
