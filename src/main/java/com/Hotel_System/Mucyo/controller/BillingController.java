package com.Hotel_System.Mucyo.controller;

import com.Hotel_System.Mucyo.dto.BillingResponse;
import com.Hotel_System.Mucyo.service.BillingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {
    private final BillingService billingService;

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<BillingResponse> getBillingByBookingId(@PathVariable Long bookingId) {
        log.info("Fetching billing for booking: {}", bookingId);
        return ResponseEntity.ok(billingService.getBillingByBookingId(bookingId));
    }
} 