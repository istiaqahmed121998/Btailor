package com.backend.productservice.domain.category.repository;

import com.backend.productservice.domain.category.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();

    Optional<Category> findByName(String name);
}
