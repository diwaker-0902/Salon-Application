package com.salon.modal;

import java.time.LocalDateTime;
import java.util.Set;

import com.salon.domain.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long salonId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ElementCollection    // by providing ElementCollection, it will store data in the separate table
    private Set<Long> serviceIds;

    private BookingStatus status = BookingStatus.PENDING; // By default, the booking status is set to PENDING

    private int totalServices;

    private int totalPrice;

    
}
