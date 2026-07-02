package com.salon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salon.dto.SalonDTO;
import com.salon.modal.Category;
import com.salon.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

    private final CategoryService categoryService;

    // @PostMapping("/salon/{id}")
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {

        SalonDTO salonDTO = new SalonDTO(); // Static creation of SalonDTO for demonstration purposes
        salonDTO.setId(1L); // Assuming you have a way to get the salon ID
        Category savedCategory = categoryService.saveCategory(category, salonDTO);
        return ResponseEntity.ok(savedCategory);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) throws Exception {

        SalonDTO salonDTO = new SalonDTO(); // Static creation of SalonDTO for demonstration purposes
        salonDTO.setId(1L); // Assuming you have a way to get the salon ID
        categoryService.deleteCategoryById(id, salonDTO.getId());
        return ResponseEntity.ok("Category deleted successfully");
    }

}
