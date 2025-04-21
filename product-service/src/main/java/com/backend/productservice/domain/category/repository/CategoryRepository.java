package com.backend.productservice.domain.category.repository;

import com.backend.productservice.domain.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    Page<Category> findAll(@NonNull Pageable pageable);
    Optional<Category> findByName(String name);
}
