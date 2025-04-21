package com.backend.productservice.application.category;

import com.backend.productservice.application.category.dto.CategoryDTO;
import com.backend.productservice.application.category.dto.CategoryResponse;
import com.backend.productservice.application.category.mapper.CategoryMapper;
import com.backend.productservice.domain.category.repository.CategoryRepository;
import com.backend.productservice.domain.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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



    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(CategoryMapper::toResponse);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

}
