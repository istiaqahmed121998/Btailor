package com.backend.productservice.domain.Tag.repository;

import com.backend.productservice.domain.Tag.model.Tag;

import java.util.Optional;

public interface TagRepository {
    Tag findById(Long id);
    Optional<Tag> findByName(String name);

    Tag save(Tag tag);
}
