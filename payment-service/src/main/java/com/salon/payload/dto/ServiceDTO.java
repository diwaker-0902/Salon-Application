package com.salon.payload.dto;

import lombok.Data;

@Data
public class ServiceDTO {

    private Long id;

    private String name;

    private String description;

    private int price;

    private int duration; // Duration in minutes

    private Long salonId;

    private Long category;

    private String image;

}
