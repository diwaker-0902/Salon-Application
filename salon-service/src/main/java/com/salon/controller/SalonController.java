package com.salon.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salon.mapper.SalonMapper;
import com.salon.modal.Salon;
import com.salon.payload.dto.SalonDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.service.SalonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    
    private final SalonService salonService;

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        Salon salon = salonService.createSalon(salonDTO, userDTO);
        SalonDTO salonDTO1 = SalonMapper.mapToDTO(salon);

        return ResponseEntity.ok(salonDTO1);
    }

    @GetMapping
    public ResponseEntity<List<SalonDTO>> getSalons() throws Exception {
        
        List<Salon> salon = salonService.getAllSalons();

        // Convert salon list into SalonDTO list
        List<SalonDTO> salonDTOs = salon.stream().map((salons) ->
            {
                SalonDTO salonDTO = SalonMapper.mapToDTO(salons);
                return salonDTO;
            }
        ).toList();

        return ResponseEntity.ok(salonDTOs);
    }


    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId) throws Exception {

       Salon salon = salonService.getSalonById(salonId);

       // Convert salon into salonDTO
       SalonDTO salonDTO = SalonMapper.mapToDTO(salon);

        return ResponseEntity.ok(salonDTO);
    }


    @PatchMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(
        @PathVariable Long salonId,
        @RequestBody SalonDTO salonDTO) throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        Salon salon = salonService.updateSalon(salonDTO, userDTO, salonId);
        SalonDTO salonDTO1 = SalonMapper.mapToDTO(salon);

        return ResponseEntity.ok(salonDTO1);
    }


// http://localhost:5002/api/salons/search?city=mumbai
@GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalons(@RequestParam("city") String city) throws Exception {

       List<Salon> salons = salonService.searchSalonByCity(city);

       // Convert salon  list into SalonDTOs list
        List<SalonDTO> salonDTOs = salons.stream().map((salon) ->
            {
                SalonDTO salonDTO = SalonMapper.mapToDTO(salon);
                return salonDTO;
            }
        ).toList();

        return ResponseEntity.ok(salonDTOs);
    }



    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId(@PathVariable Long salonId) throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

       Salon salon = salonService.getSalonByOwnerId(userDTO.getId());

       // Convert salon into salonDTO
       SalonDTO salonDTO = SalonMapper.mapToDTO(salon);

        return ResponseEntity.ok(salonDTO);
    }
}
