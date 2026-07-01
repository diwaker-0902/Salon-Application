package com.salon.repository;

import java.util.List;

import com.salon.modal.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SalonRepository extends JpaRepository<Salon, Long> {

    Salon findByOwnerId(Long id);

    @Query("select s from Salon s where" + 
        "(lower(s.city) like lower(concat('%', :keyword, '%')) OR " + 
        "lower(s.name) like lower(concat('%', :keyword, '%')) OR " +
        "lower(s.address) like lower(concat('%', :keyword, '%')))"
    )
    List<Salon> searchSalons(@Param("keyword") String keyword);


}
