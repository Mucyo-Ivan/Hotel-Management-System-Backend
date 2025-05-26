package com.Hotel_System.Mucyo.service;

import com.Hotel_System.Mucyo.dto.BillingResponse;
import com.Hotel_System.Mucyo.model.Billing;
import com.Hotel_System.Mucyo.model.Booking;
import com.Hotel_System.Mucyo.model.Hotel;
import com.Hotel_System.Mucyo.model.Room;
import com.Hotel_System.Mucyo.model.User;
import com.Hotel_System.Mucyo.repository.BillingRepository;
import com.Hotel_System.Mucyo.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {
    private final BillingRepository billingRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public BillingResponse createBillingForBooking(Booking booking) {
        log.info("Creating billing for booking: {}", booking.getId());

        Billing existingBilling = billingRepository.findByBookingId(booking.getId());
        if (existingBilling != null) {
            log.info("Billing already exists for booking: {}. Returning existing one.", booking.getId());
            return mapToBillingResponse(existingBilling);
        }

        long nights = Duration.between(booking.getCheckInDate().atStartOfDay(),
                                     booking.getCheckOutDate().atStartOfDay()).toDays();
        double totalAmount = booking.getRoom().getPrice() * nights;

        Billing billing = new Billing();
        billing.setBooking(booking);
        billing.setAmount(totalAmount);
        billing.setGeneratedAt(LocalDateTime.now());

        Billing savedBilling = billingRepository.save(billing);
        log.info("Billing created successfully with id: {}", savedBilling.getId());
        return mapToBillingResponse(savedBilling);
    }

    @Transactional
    public BillingResponse getBillingByBookingId(Long bookingId) {
        log.info("Fetching billing for booking: {}", bookingId);

        Billing billing = billingRepository.findByBookingId(bookingId);

        if (billing == null) {
            log.info("No billing found for booking: {}, attempting to create new billing", bookingId);
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId + " when trying to create billing."));
            return createBillingForBooking(booking);
        }
        log.info("Found existing billing for booking: {}", bookingId);
        return mapToBillingResponse(billing);
    }

    private BillingResponse mapToBillingResponse(Billing billing) {
        Booking booking = billing.getBooking();
        User user = booking.getUser();
        Room room = booking.getRoom();
        Hotel hotel = room.getHotel(); // This will be initialized within the transaction

        return BillingResponse.builder()
                .id(billing.getId())
                .amount(billing.getAmount())
                .generatedAt(billing.getGeneratedAt())
                .bookingId(booking.getId())
                .userName(user != null ? user.getName() : null)
                .roomNumber(room != null ? room.getRoomNumber() : null)
                .hotelName(hotel != null ? hotel.getName() : null) // Safely access name
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .bookingStatus(booking.getStatus())
                .build();
    }
} 