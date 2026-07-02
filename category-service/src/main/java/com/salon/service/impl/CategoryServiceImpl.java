package com.salon.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.salon.dto.SalonDTO;
import com.salon.modal.Category;
import com.salon.repository.CategoryRepository;
import com.salon.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository; // either use @Autowired or constructor injection, but not both

    // public CategoryServiceImpl(CategoryRepository categoryRepository) {
    //     this.categoryRepository = categoryRepository;
    // }

    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {
        
        Category newCategory = new Category();

        newCategory.setName(category.getName());
        newCategory.setImage(category.getImage());
        newCategory.setSalonId(salonDTO.getId()); // Assuming SalonDTO has a get

        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoriesBySalon(Long id) {
        return categoryRepository.findBySalonId(id);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElse(null);

                if (category == null) {
                    throw new Exception("Category not found with id: " + id);
                }

        return category;
    }

    @Override
    public void deleteCategoryById(Long id, Long salonId) throws Exception {
        
        Category category = getCategoryById(id); // Check if the category exists before deleting
        
        if (!category.getSalonId().equals(salonId)) {
            throw new Exception("You don't have permission to delete this category");
        }

        categoryRepository.deleteById(id);
    }



}
