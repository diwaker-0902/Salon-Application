package com.salon.service;

import java.util.List;

import com.salon.modal.Salon;
import com.salon.payload.dto.SalonDTO;
import com.salon.payload.dto.UserDTO;

public interface SalonService {

    Salon createSalon(SalonDTO salon, UserDTO user); // providing UserDTO says that who is the owner of the salon 

    Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception; // UserDTO provided here so that only salon user will able to update else throw exception

    List<Salon> getAllSalons();

    Salon getSalonById(Long salonId) throws Exception;

    Salon getSalonByOwnerId(Long ownerId);

    List<Salon> searchSalonByCity(String city); 
    
}