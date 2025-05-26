package com.Hotel_System.Mucyo.repository;

import com.Hotel_System.Mucyo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    List<Room> findByHotelIdAndIsAvailableTrue(Long hotelId);
    boolean existsByHotelIdAndRoomType(Long hotelId, String roomType);
} 