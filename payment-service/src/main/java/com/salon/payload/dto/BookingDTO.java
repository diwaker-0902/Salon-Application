package com.salon.payload.dto;

import java.time.LocalDateTime;
import java.util.Set;

// import com.salon.domain.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Long id;

    private Long salonId;

    private Long customerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Long> serviceIds;

    // private BookingStatus status = BookingStatus.PENDING;
    private int totalPrice;

}
