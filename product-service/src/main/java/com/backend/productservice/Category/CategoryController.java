package com.backend.productservice.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/name/{name}")
    public Optional<Category> getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    @PostMapping
    public Category createCategory(@RequestBody CategoryDTO dto) {
        return categoryService.createCategory(dto);
    }
}
