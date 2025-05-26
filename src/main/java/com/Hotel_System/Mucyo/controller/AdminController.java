package com.Hotel_System.Mucyo.controller;

import com.Hotel_System.Mucyo.dto.AdminRegistrationCompletionRequest;
import com.Hotel_System.Mucyo.dto.HotelDTO;
import com.Hotel_System.Mucyo.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final HotelService hotelService;

    @PostMapping("/complete-registration")
    public ResponseEntity<HotelDTO> completeRegistration(@Valid @RequestBody AdminRegistrationCompletionRequest request) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setName(request.getHotelName());
        hotelDTO.setLocation(request.getLocation());
        return ResponseEntity.ok(hotelService.createHotel(hotelDTO));
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(hotelService.getAllBookings());
    }

    @GetMapping("/bookings/status/{status}")
    public ResponseEntity<?> getBookingsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(hotelService.getBookingsByStatus(status));
    }
} 