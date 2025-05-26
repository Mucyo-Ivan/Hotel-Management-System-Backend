package com.Hotel_System.Mucyo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminRegistrationCompletionRequest {
    @NotBlank(message = "Hotel name is required")
    private String hotelName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Number of rooms is required")
    private Integer numberOfRooms;

    @NotBlank(message = "Hotel type is required")
    private String hotelType; // e.g., "LUXURY", "BUSINESS", "BUDGET"
} 