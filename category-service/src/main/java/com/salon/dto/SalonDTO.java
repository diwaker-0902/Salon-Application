// package com.salon.dto;

// public class SalonDTO {

//     private Long id;

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

// }
package com.salon.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class SalonDTO {

    private Long id;

    private String name;

    private List<String> images;    // Cloudinary will return url and we store url in db

    private String address;

    private String phoneNumber;

    private String email;

    private String city;

    private Long ownerId;

    // private UserDTO owner;

    private LocalTime openTime;

    private LocalTime closeTime;

}
