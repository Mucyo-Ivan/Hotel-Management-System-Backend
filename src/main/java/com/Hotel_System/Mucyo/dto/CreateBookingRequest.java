package com.Hotel_System.Mucyo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateBookingRequest {
    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotNull(message = "Check-in date is required")
    private LocalDateTime checkIn;

    @NotNull(message = "Check-out date is required")
    private LocalDateTime checkOut;
} 