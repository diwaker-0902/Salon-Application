package com.salon.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salon.modal.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Set<Category> findBySalonId(Long salonId);


}
