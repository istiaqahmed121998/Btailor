package com.backend.productservice.infrastructure.persistence.category;

import com.backend.productservice.domain.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaCategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
