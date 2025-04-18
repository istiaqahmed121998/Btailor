package com.backend.productservice.interfaces.rest.category;

import com.backend.productservice.application.category.CategoryApplicationService;
import com.backend.productservice.application.category.dto.CategoryDTO;
import com.backend.productservice.domain.category.model.Category;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    private final CategoryApplicationService categoryService;

    public CategoryController(CategoryApplicationService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/")
    public Category createCategory(@RequestBody CategoryDTO dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }



}
