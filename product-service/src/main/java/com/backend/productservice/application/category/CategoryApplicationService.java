package com.backend.productservice.application.category;

import com.backend.productservice.application.category.dto.CategoryDTO;
import com.backend.productservice.domain.category.repository.CategoryRepository;
import com.backend.productservice.domain.category.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryApplicationService {
    private final CategoryRepository categoryRepository;

    public CategoryApplicationService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.CategoryName());

        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            if (categoryDTO.ParentCategoryName() != null) {
                categoryRepository.findByName(categoryDTO.ParentCategoryName())
                        .ifPresentOrElse(category::setParentCategory,()->{
                            Category categoryParent = new Category();
                            categoryParent.setName(categoryDTO.ParentCategoryName());
                        });
            }
            return categoryRepository.save(category);
        } else {
            Category newCategory = new Category();
            newCategory.setName(categoryDTO.CategoryName());
            if (categoryDTO.ParentCategoryName() != null) {
                categoryRepository.findByName(categoryDTO.ParentCategoryName())
                        .ifPresentOrElse(newCategory::setParentCategory,()->{
                            Category categoryParent = new Category();
                            categoryParent.setName(categoryDTO.ParentCategoryName());
                            newCategory.setParentCategory(categoryRepository.save(categoryParent));
                        });
            }

            return categoryRepository.save(newCategory);
        }
    }



    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

}
