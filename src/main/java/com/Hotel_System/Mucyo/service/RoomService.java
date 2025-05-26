package com.Hotel_System.Mucyo.service;

import com.Hotel_System.Mucyo.dto.RoomDTO;
import com.Hotel_System.Mucyo.exception.ResourceNotFoundException;
import com.Hotel_System.Mucyo.model.Hotel;
import com.Hotel_System.Mucyo.model.Room;
import com.Hotel_System.Mucyo.repository.HotelRepository;
import com.Hotel_System.Mucyo.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Transactional
    public RoomDTO createRoom(RoomDTO roomDTO) {
        log.info("Creating room: {}", roomDTO.getRoomNumber());
        Hotel hotel = hotelRepository.findById(roomDTO.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + roomDTO.getHotelId()));

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setRoomType(roomDTO.getRoomType());
        room.setPrice(roomDTO.getPrice());
        room.setIsAvailable(roomDTO.getIsAvailable() != null ? roomDTO.getIsAvailable() : true);
        room.setDescription(roomDTO.getDescription());
        room.setMaxOccupancy(roomDTO.getMaxOccupancy());

        Room savedRoom = roomRepository.save(room);
        log.info("Room created successfully with id: {}", savedRoom.getId());
        return convertToDTO(savedRoom);
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> getAllRooms() {
        log.info("Fetching all rooms");
        return roomRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomDTO getRoomById(Long id) {
        log.info("Fetching room with id: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return convertToDTO(room);
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> getRoomsByHotel(Long hotelId) {
        log.info("Fetching rooms for hotel: {}", hotelId);
        return roomRepository.findByHotelId(hotelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        log.info("Updating room with id: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setRoomType(roomDTO.getRoomType());
        room.setPrice(roomDTO.getPrice());
        room.setIsAvailable(roomDTO.getIsAvailable());
        room.setDescription(roomDTO.getDescription());
        room.setMaxOccupancy(roomDTO.getMaxOccupancy());

        Room updatedRoom = roomRepository.save(room);
        log.info("Room updated successfully");
        return convertToDTO(updatedRoom);
    }

    @Transactional
    public void deleteRoom(Long id) {
        log.info("Deleting room with id: {}", id);
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
        log.info("Room deleted successfully");
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setPrice(room.getPrice());
        dto.setIsAvailable(room.getIsAvailable());
        dto.setDescription(room.getDescription());
        dto.setMaxOccupancy(room.getMaxOccupancy());
        dto.setHotelId(room.getHotel().getId());
        return dto;
    }
} 