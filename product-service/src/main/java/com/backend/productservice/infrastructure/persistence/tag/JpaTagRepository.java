package com.backend.productservice.infrastructure.persistence.tag;

import com.backend.productservice.domain.Tag.model.Tag;
import com.backend.productservice.domain.Tag.repository.TagRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaTagRepository implements TagRepository {

    private final SpringDataJpaTagRepository jpaRepo;

    public JpaTagRepository(SpringDataJpaTagRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Tag findById(Long id) {
        return jpaRepo.findById(id).orElse(null);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return jpaRepo.findByName(name);
    }

    @Override
    public Tag save(Tag tag) {
        return jpaRepo.save(tag);
    }
}
