package com.Hotel_System.Mucyo.service;

import com.Hotel_System.Mucyo.dto.BookingRequest;
import com.Hotel_System.Mucyo.dto.BookingResponse;
import com.Hotel_System.Mucyo.exception.ResourceNotFoundException;
import com.Hotel_System.Mucyo.model.Booking;
import com.Hotel_System.Mucyo.model.Room;
import com.Hotel_System.Mucyo.model.User;
import com.Hotel_System.Mucyo.repository.BookingRepository;
import com.Hotel_System.Mucyo.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final NotificationService notificationService;

    @Transactional
    public BookingResponse createBooking(User user, BookingRequest request) {
        log.info("Creating booking for user: {} and room: {} in hotel: {}", 
                user.getEmail(), request.getRoomId(), request.getHotelId());
        
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // Validate that the room belongs to the specified hotel
        if (!room.getHotel().getId().equals(request.getHotelId())) {
            throw new IllegalStateException("Room does not belong to the specified hotel");
        }

        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is not available");
        }

        // Check if room is already booked for the requested dates
        boolean isRoomBooked = bookingRepository.existsByRoomAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                room, request.getCheckOutDate(), request.getCheckInDate());
        
        if (isRoomBooked) {
            throw new IllegalStateException("Room is already booked for the selected dates");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalPrice(calculateTotalPrice(room, request.getCheckInDate(), request.getCheckOutDate()));
        booking.setStatus("CONFIRMED");
        booking.setCreatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created successfully with id: {}", savedBooking.getId());
        
        return mapToBookingResponse(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(User user) {
        log.info("Fetching bookings for user: {}", user.getEmail());
        return bookingRepository.findByUser(user).stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll().stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingResponse getBooking(Long id) {
        log.info("Fetching booking with id: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return mapToBookingResponse(booking);
    }

    @Transactional
    public BookingResponse updateBooking(Long id, BookingRequest request) {
        log.info("Updating booking with id: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // Validate that the room belongs to the specified hotel
        if (!room.getHotel().getId().equals(request.getHotelId())) {
            throw new IllegalStateException("Room does not belong to the specified hotel");
        }

        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalPrice(calculateTotalPrice(room, request.getCheckInDate(), request.getCheckOutDate()));

        Booking updatedBooking = bookingRepository.save(booking);
        return mapToBookingResponse(updatedBooking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        log.info("Deleting booking with id: {}", id);
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    public boolean isBookingOwner(Long bookingId, User user) {
        return bookingRepository.existsByIdAndUser(bookingId, user);
    }

    @Transactional
    public BookingResponse cancelBooking(Long id, User user) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Verify user has permission to cancel
        if (!user.getRole().equals("ADMIN") && !booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User not authorized to cancel this booking");
        }

        // Check if booking can be cancelled
        if (booking.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Booking is already cancelled");
        }

        if (booking.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("Cannot cancel a completed booking");
        }

        // Update booking status
        booking.setStatus("CANCELLED");
        Booking savedBooking = bookingRepository.save(booking);

        // Create notification for cancellation
        notificationService.createNotification(
            booking.getUser().getId(),
            String.format("Your booking for Room %s has been cancelled", booking.getRoom().getRoomNumber()),
            "BOOKING_CANCELLATION"
        );

        return mapToBookingResponse(savedBooking);
    }

    private double calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        return room.getPrice() * days;
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .userName(booking.getUser().getName())
                .roomId(booking.getRoom().getId())
                .roomNumber(booking.getRoom().getRoomNumber())
                .hotelId(booking.getRoom().getHotel().getId())
                .hotelName(booking.getRoom().getHotel().getName())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .build();
    }
} 