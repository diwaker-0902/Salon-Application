package com.salon.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salon.domain.BookingStatus;
import com.salon.dto.BookingRequest;
import com.salon.dto.SalonDTO;
import com.salon.dto.ServiceDTO;
import com.salon.dto.UserDTO;
import com.salon.modal.Booking;
import com.salon.modal.SalonReport;
import com.salon.repository.BookingRepository;
import com.salon.service.BookingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookingRequest booking, UserDTO userDTO, SalonDTO salonDTO,
            List<ServiceDTO> serviceDTOSet) {
        // Before create a booking, check if the salon is available for the requested date and time

                int totalDuration = serviceDTOSet.stream()
                        .mapToInt(ServiceDTO::getDuration)
                        .sum();
  
                        LocalDateTime bookingStartTime = booking.getStartTime();
                        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

                        // Boolean isSlotAvailable = isTimeSlotAvailable(salon, bookingStartTime, bookingEndTime);

                        try {
            isTimeSlotAvailable(salonDTO, bookingStartTime, bookingEndTime);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

                        int totalPrice = serviceDTOSet.stream()
                                .mapToInt(ServiceDTO::getPrice)
                                .sum();
                                
                                Set<Long> idList = serviceDTOSet.stream()
                                        .map(ServiceDTO::getId)
                                        .collect(Collectors.toSet());

        Booking newBooking = new Booking();
                newBooking.setCustomerId(userDTO.getId());
                newBooking.setSalonId(salonDTO.getId());
                newBooking.setServiceIds(idList);
                newBooking.setStatus(BookingStatus.PENDING);
                newBooking.setStartTime(bookingStartTime);
                newBooking.setEndTime(bookingEndTime);
                newBooking.setTotalPrice(totalPrice);
                // newBooking.setTotalServices(totalPrice);
                

        // throw new UnsupportedOperationException("Unimplemented method 'createBooking'");
        return bookingRepository.save(newBooking);
    }

    public Boolean isTimeSlotAvailable(SalonDTO salonDTO,
                                        LocalDateTime bookingStartTime, 
                                        LocalDateTime bookingEndTime) throws Exception {

        List<Booking> existingBookings = getBookingsBySalon(salonDTO.getId());

        // Check if the salon is available for the requested date and time
        LocalDateTime salonOpenTime = salonDTO.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salonDTO.getCloseTime().atDate(bookingStartTime.toLocalDate());
    
                                            
        if (bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)) {
            throw new Exception("Booking time is outside of salon operating hours.");
        }

        for (Booking existingBooking : existingBookings) {
            LocalDateTime existingBookingStartTime = existingBooking.getStartTime();
            LocalDateTime existingBookingEndTime = existingBooking.getEndTime();
    
            if (bookingStartTime.isBefore(existingBookingEndTime) && bookingEndTime.isAfter(existingBookingStartTime)) {
                throw new Exception("The requested time slot is already booked.");
            }

            if (bookingStartTime.isEqual(existingBookingStartTime) || bookingEndTime.isEqual(existingBookingEndTime)) {
                throw new Exception("The requested time slot is already booked.");
            }
        }
        
        return true;

    }

    @Override
    public List<Booking> getBookingsByCustomer(Long customerId) {

        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElse(null);
        if (booking == null) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        return booking;
    }

    @Override
    public Booking updateBooking(Long bookingId, BookingStatus status) {
       Booking booking = getBookingById(bookingId);

        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBooking'");
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {
                
        List<Booking> allBookings = getBookingsBySalon(salonId);

        if (date == null) {
            return allBookings;
        }

        return allBookings.stream()
                .filter(booking -> isSameDate(booking.getStartTime(), date) || 
                        isSameDate(booking.getEndTime(), date))
                        .collect(Collectors.toList());
    }

    public boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }



    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking> bookings = getBookingsBySalon(salonId);

        int totalEarnings = bookings.stream()
        .mapToInt(Booking::getTotalPrice)
        .sum();
        
        int totalBooking = bookings.size();
        
        List<Booking> cancelledBookings = bookings.stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.CANCELLED))
                .collect(Collectors.toList());

        Double totalRefund = cancelledBookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        SalonReport report = new SalonReport();
        report.setSalonId(salonId);
        report.setCancelledBookings(cancelledBookings.size());
        report.setTotalBookings(totalBooking);
        report.setTotalEarnings(totalEarnings);
        report.setTotalRefund(totalRefund);
        report.setTotalBookings(totalBooking);
        

        return report;
    }

    

    

    // Implement the methods defined in the BookingService interface

}
