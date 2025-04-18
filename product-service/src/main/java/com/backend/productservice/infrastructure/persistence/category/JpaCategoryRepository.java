package com.backend.productservice.infrastructure.persistence.category;

import com.backend.productservice.domain.category.model.Category;
import com.backend.productservice.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class JpaCategoryRepository implements CategoryRepository {

    private final SpringDataJpaCategoryRepository jpaRepo;

    public JpaCategoryRepository(SpringDataJpaCategoryRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }


    @Override
    public Category save(Category category) {
        return jpaRepo.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepo.findAll();
    }

    @Override
    public Optional<Category> findByName(String name) {
        return jpaRepo.findByName(name);
    }
}