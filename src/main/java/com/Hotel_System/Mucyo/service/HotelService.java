package com.Hotel_System.Mucyo.service;

import com.Hotel_System.Mucyo.dto.HotelDTO;
import com.Hotel_System.Mucyo.exception.ResourceNotFoundException;
import com.Hotel_System.Mucyo.model.Booking;
import com.Hotel_System.Mucyo.model.Hotel;
import com.Hotel_System.Mucyo.repository.BookingRepository;
import com.Hotel_System.Mucyo.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public HotelDTO createHotel(HotelDTO hotelDTO) {
        log.info("Creating hotel: {}", hotelDTO.getName());
        if (hotelRepository.existsByNameAndLocation(hotelDTO.getName(), hotelDTO.getLocation())) {
            throw new IllegalArgumentException("Hotel already exists at this location");
        }

        Hotel hotel = new Hotel();
        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setAddress(hotelDTO.getAddress());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setHotelType(hotelDTO.getHotelType());
        hotel.setNumberOfRooms(hotelDTO.getNumberOfRooms());
        hotel.setPhoneNumber(hotelDTO.getPhoneNumber());

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully with id: {}", savedHotel.getId());
        return convertToDTO(savedHotel);
    }

    @Transactional(readOnly = true)
    public List<HotelDTO> getAllHotels() {
        log.info("Fetching all hotels");
        return hotelRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HotelDTO getHotelById(Long id) {
        log.info("Fetching hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return convertToDTO(hotel);
    }

    @Transactional(readOnly = true)
    public List<HotelDTO> getHotelsByLocation(String location) {
        log.info("Fetching hotels in location: {}", location);
        return hotelRepository.findByLocation(location).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public HotelDTO updateHotel(Long id, HotelDTO hotelDTO) {
        log.info("Updating hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setAddress(hotelDTO.getAddress());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setHotelType(hotelDTO.getHotelType());
        hotel.setNumberOfRooms(hotelDTO.getNumberOfRooms());
        hotel.setPhoneNumber(hotelDTO.getPhoneNumber());

        Hotel updatedHotel = hotelRepository.save(hotel);
        log.info("Hotel updated successfully");
        return convertToDTO(updatedHotel);
    }

    @Transactional
    public void deleteHotel(Long id) {
        log.info("Deleting hotel with id: {}", id);
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
        log.info("Hotel deleted successfully");
    }

    public List<Booking> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByStatus(String status) {
        log.info("Fetching bookings with status: {}", status);
        return bookingRepository.findByStatus(status);
    }

    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setLocation(hotel.getLocation());
        dto.setAddress(hotel.getAddress());
        dto.setDescription(hotel.getDescription());
        dto.setHotelType(hotel.getHotelType());
        dto.setNumberOfRooms(hotel.getNumberOfRooms());
        dto.setPhoneNumber(hotel.getPhoneNumber());
        return dto;
    }
} 