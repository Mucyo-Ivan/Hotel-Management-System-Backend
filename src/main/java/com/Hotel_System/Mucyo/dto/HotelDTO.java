package com.Hotel_System.Mucyo.dto;

import lombok.Data;

@Data
public class HotelDTO {
    private Long id;
    private String name;
    private String location;
    private String address;
    private String description;
    private String hotelType;
    private Integer numberOfRooms;
    private String phoneNumber;
} 