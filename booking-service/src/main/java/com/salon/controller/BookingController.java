package com.salon.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salon.domain.BookingStatus;
import com.salon.dto.BookingDTO;
import com.salon.dto.BookingRequest;
import com.salon.dto.BookingSlotDTO;
import com.salon.dto.SalonDTO;
import com.salon.dto.ServiceDTO;
import com.salon.dto.UserDTO;
import com.salon.mapper.BookingMapper;
import com.salon.modal.Booking;
import com.salon.modal.SalonReport;
import com.salon.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    public final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(
        @RequestParam Long salonId,
        @RequestBody BookingRequest bookingRequest
    ) {
        UserDTO user = new UserDTO();
        user.setId(1L);

        SalonDTO salon = new SalonDTO();
        salon.setId(salonId);
        // salon.setOpenTime(LocalTime.now());
        // salon.setCloseTime(LocalTime.now().plusHours(12));

        // FIXED: Hardcode stable operating hours so your test inputs don't fail based on execution time
    salon.setOpenTime(LocalTime.of(9, 0));  // Salon opens at 09:00 AM
    salon.setCloseTime(LocalTime.of(21, 0)); // Salon closes at 09:00 PM

        // salon.setOpenTime("2026-07-20T10:00:00");

        // Set<ServiceDTO> serviceDTOSet = new HashSet<>();
        List<ServiceDTO> serviceDTOList = new ArrayList<>();

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setPrice(222);
        serviceDTO.setDuration(45);
        serviceDTO.setName("Hair Cut");

        // serviceDTOSet.add(serviceDTO);
        serviceDTOList.add(serviceDTO);

        // Booking booking = bookingService.createBooking(bookingRequest, user, salon, serviceDTOSet);
        Booking booking = bookingService.createBooking(bookingRequest, user, salon, serviceDTOList);


        return ResponseEntity.ok(booking);
    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer() {

        List<Booking> bookings = bookingService.getBookingsByCustomer(1L);

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon() {

        List<Booking> bookings = bookingService.getBookingsBySalon(1L);

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
        return bookings.stream()
            .map(booking->{
                return BookingMapper.toDTO(booking);
            }).collect(Collectors.toSet());
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId) throws Exception {

        Booking bookings = bookingService.getBookingById(bookingId);

        return ResponseEntity.ok(BookingMapper.toDTO(bookings));
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingById(@PathVariable Long bookingId,
        @RequestParam BookingStatus status
    ) throws Exception {

        Booking booking = bookingService.updateBooking(bookingId, status);

        return ResponseEntity.ok(BookingMapper.toDTO(booking));

    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookedSlot(
        @PathVariable Long salonId,
        // @RequestParam(required = false) LocalDate date
        // FIXED: Changed from @RequestParam to @PathVariable to match the URL mapping
    @PathVariable @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date
    ) throws Exception {

        List<Booking> bookings = bookingService.getBookingsByDate(date, salonId);

        List<BookingSlotDTO> slotsDTOs = bookings.stream()
                        .map(booking-> {
                            BookingSlotDTO slotDTO = new BookingSlotDTO();
                            slotDTO.setStartTime(booking.getStartTime());
                            slotDTO.setEndTime(booking.getEndTime());
                            return slotDTO;
                        }).collect(Collectors.toList());

        return ResponseEntity.ok(slotsDTOs);
    }



    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport() throws Exception {

        SalonReport report = bookingService.getSalonReport(1L);

        return ResponseEntity.ok(report);
    }
}
