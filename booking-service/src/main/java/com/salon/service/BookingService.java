package com.salon.service;

import java.time.LocalDate;
import java.util.List;

import com.salon.domain.BookingStatus;
import com.salon.dto.BookingRequest;
import com.salon.dto.SalonDTO;
import com.salon.dto.ServiceDTO;
import com.salon.dto.UserDTO;
import com.salon.modal.Booking;
import com.salon.modal.SalonReport;

public interface BookingService {

    Booking createBooking(BookingRequest booking,
                            UserDTO userDTO,
                            SalonDTO salonDTO,
                            List<ServiceDTO> serviceDTOSet

    );

    List<Booking> getBookingsByCustomer(Long customerId);

    List<Booking> getBookingsBySalon(Long salonId);

    Booking getBookingById(Long id);

    Booking updateBooking(Long bookingId, BookingStatus status);

    void deleteBooking(Long bookingId);

    List<Booking> getBookingsByDate(LocalDate date, Long salonId);

    SalonReport getSalonReport(Long salonId);






}
