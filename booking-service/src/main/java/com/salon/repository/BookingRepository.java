package com.salon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salon.modal.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findBySalonId(Long salonId);
    

}
