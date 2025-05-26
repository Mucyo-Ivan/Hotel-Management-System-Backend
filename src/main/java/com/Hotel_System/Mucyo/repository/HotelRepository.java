package com.Hotel_System.Mucyo.repository;

import com.Hotel_System.Mucyo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
 
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByLocation(String location);
    boolean existsByNameAndLocation(String name, String location);
} 