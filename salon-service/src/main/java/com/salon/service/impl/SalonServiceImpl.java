package com.salon.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.salon.modal.Salon;
import com.salon.payload.dto.SalonDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.service.SalonService;

import lombok.RequiredArgsConstructor;

import com.salon.repository.SalonRepository;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    // Constructor Injection or use @RequiredArgsConstructor
    // public SalonServiceImpl(SalonRepository salonRepository) {
    //     this.salonRepository = salonRepository;
    // }

    @Override
    public Salon createSalon(SalonDTO req, UserDTO user) {
            Salon salon = new Salon();
            salon.setName(req.getName());
            salon.setAddress(req.getAddress());
            salon.setEmail(req.getEmail());
            salon.setCity(req.getCity());
            salon.setImages(req.getImages());
            salon.setOwnerId(user.getId());
            salon.setOpenTime(req.getOpenTime());
            salon.setCloseTime(req.getCloseTime());
            salon.setPhoneNumber(req.getPhoneNumber());

            return salonRepository.save(salon);
    }


    @Override
    public Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception {
        
        Salon existingSalon = salonRepository.findById(salonId).orElse(null);

        if (!salon.getOwnerId().equals(user.getId())) {
            throw new Exception("You don't have permission to update this salon");
        }
        if (existingSalon != null) { // Only salon owner can update
                existingSalon.setCity(salon.getCity());
                existingSalon.setName(salon.getName());
                existingSalon.setAddress(salon.getAddress());
                existingSalon.setEmail(salon.getEmail());
                existingSalon.setImages(salon.getImages());
                existingSalon.setOpenTime(salon.getOpenTime());
                existingSalon.setCloseTime(salon.getCloseTime());
                existingSalon.setOwnerId(user.getId());
                existingSalon.setPhoneNumber(salon.getPhoneNumber());

                return salonRepository.save(existingSalon);
        }

        throw new Exception("Salon not exists");
    }

    @Override
    public List<Salon> getAllSalons() {
            return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) throws Exception {
            Salon salon = salonRepository.findById(salonId).orElse(null);

            if (salon == null) {
                throw new Exception("Salon not exist");
            }

            return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {

        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCity(String city) {
        
        return salonRepository.searchSalons(city);
    }

}
