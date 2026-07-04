package com.salon.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salon.dto.CategoryDTO;
import com.salon.dto.SalonDTO;
import com.salon.dto.ServiceDTO;
import com.salon.modal.ServiceOffering;
import com.salon.repository.ServiceOfferingRepository;
import com.salon.service.ServiceOfferingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {
        
        ServiceOffering serviceOffering = new ServiceOffering();
        serviceOffering.setImage(serviceDTO.getImage());
        serviceOffering.setSalonId(salonDTO.getId());
        serviceOffering.setName(serviceDTO.getName());
        serviceOffering.setDescription(serviceDTO.getDescription());
        serviceOffering.setCategoryId(categoryDTO.getId());
        serviceOffering.setPrice(serviceDTO.getPrice());
        serviceOffering.setDuration(serviceDTO.getDuration());
        serviceOfferingRepository.save(serviceOffering);
        return serviceOffering;
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception {
        
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceId).orElse(null);

        if (serviceOffering == null) {
            throw new Exception("Service not exist with id: " + serviceId);
        }

            serviceOffering.setImage(service.getImage());
            serviceOffering.setName(service.getName());
            serviceOffering.setDescription(service.getDescription());
            // serviceOffering.setCategoryId(service.getCategoryId());
            serviceOffering.setPrice(service.getPrice());
            serviceOffering.setDuration(service.getDuration());

            serviceOfferingRepository.save(serviceOffering);

        return serviceOffering;
    }

    @Override
    public Set<ServiceOffering> getAllServiceBySalonId(Long salonId, Long categoryId) {
        
        Set<ServiceOffering> services = serviceOfferingRepository.findBySalonId(salonId);

        if (categoryId != null) {
            services = services.stream()
                    .filter((service) -> service.getCategoryId() != null 
                    &&
                    service.getCategoryId()==categoryId)
                    .collect(Collectors.toSet());
                    
        }
        return services;
    }

    @Override
    public Set<ServiceOffering> getAllServicesByIds(Set<Long> ids) {

        List<ServiceOffering> services = serviceOfferingRepository.findAllById(ids);
        return new HashSet<>(services);
    }

    @Override
    public ServiceOffering getServiceById(Long id) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(id)
        .orElse(null);

        if (serviceOffering == null) {
            throw new Exception("Service not exist with id: " + id);
        }

        return serviceOffering;
    }

}
