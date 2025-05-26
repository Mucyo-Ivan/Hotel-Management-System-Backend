package com.Hotel_System.Mucyo.repository;

import com.Hotel_System.Mucyo.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Billing findByBookingId(Long bookingId);
}
