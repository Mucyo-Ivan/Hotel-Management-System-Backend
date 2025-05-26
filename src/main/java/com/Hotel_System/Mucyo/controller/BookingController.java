package com.Hotel_System.Mucyo.controller;

import com.Hotel_System.Mucyo.dto.BookingRequest;
import com.Hotel_System.Mucyo.dto.BookingResponse;
import com.Hotel_System.Mucyo.model.User;
import com.Hotel_System.Mucyo.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> createBooking(
            @AuthenticationPrincipal User user,
            @RequestBody BookingRequest request) {
        log.info("Creating booking for user: {}", user.getEmail());
        return ResponseEntity.ok(bookingService.createBooking(user, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<List<BookingResponse>> getBookings(@AuthenticationPrincipal User user) {
        log.info("Fetching bookings for user: {}", user.getEmail());
        if (user.getRole().equals("ADMIN")) {
            return ResponseEntity.ok(bookingService.getAllBookings());
        } else {
            return ResponseEntity.ok(bookingService.getUserBookings(user));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookingService.isBookingOwner(#id, authentication.principal)")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        log.info("Fetching booking with id: {}", id);
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookingService.isBookingOwner(#id, authentication.principal)")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingRequest request) {
        log.info("Updating booking with id: {}", id);
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookingService.isBookingOwner(#id, authentication.principal)")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.info("Deleting booking with id: {}", id);
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @bookingService.isBookingOwner(#id, authentication.principal)")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        log.info("Cancelling booking with id: {} for user: {}", id, user.getEmail());
        return ResponseEntity.ok(bookingService.cancelBooking(id, user));
    }
} 