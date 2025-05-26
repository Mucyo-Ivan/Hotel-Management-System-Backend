package com.Hotel_System.Mucyo.repository;

import com.Hotel_System.Mucyo.model.Booking;
import com.Hotel_System.Mucyo.model.Room;
import com.Hotel_System.Mucyo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    
    boolean existsByRoomAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
        Room room, LocalDate checkOutDate, LocalDate checkInDate);
    
    boolean existsByIdAndUser(Long id, User user);
    
    List<Booking> findByStatus(String status);
} 