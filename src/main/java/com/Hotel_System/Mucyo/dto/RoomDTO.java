package com.Hotel_System.Mucyo.dto;

import lombok.Data;

@Data
public class RoomDTO {
    private Long id;
    private String roomNumber;
    private String roomType;
    private Double price;
    private Boolean isAvailable;
    private String description;
    private Integer maxOccupancy;
    private Long hotelId;
} 