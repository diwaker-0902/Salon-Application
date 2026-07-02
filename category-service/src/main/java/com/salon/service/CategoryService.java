package com.salon.service;

import java.util.Set;

import com.salon.dto.SalonDTO;
import com.salon.modal.Category;

public interface CategoryService {

    Category saveCategory(Category category, SalonDTO salonDTO);
    Set<Category> getAllCategoriesBySalon(Long id);
    Category getCategoryById(Long id) throws Exception;
    void deleteCategoryById(Long id, Long salonId) throws Exception;

}
