package com.backend.productservice.interfaces.rest.category;

import com.backend.productservice.application.category.CategoryApplicationService;
import com.backend.productservice.application.category.dto.CategoryDTO;
import com.backend.productservice.application.category.dto.CategoryResponse;
import com.backend.productservice.application.category.mapper.CategoryMapper;
import com.backend.productservice.exception.ResourceNotFoundException;
import com.backend.productservice.utils.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    private final CategoryApplicationService categoryService;

    public CategoryController(CategoryApplicationService categoryService) {
        this.categoryService = categoryService;
    }
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    @PostMapping(value = "/")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(CategoryMapper.toResponse(categoryService.createCategory(dto)));
    }

    @GetMapping("/")
    public ResponseEntity<PaginatedResponse<CategoryResponse>> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryResponse> categoryResponses = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(new PaginatedResponse<>(HttpStatus.OK.getReasonPhrase(), "Categories", categoryResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(category -> ResponseEntity.ok(CategoryMapper.toResponse(category)))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }



}
