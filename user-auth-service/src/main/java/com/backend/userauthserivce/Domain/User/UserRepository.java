package com.backend.userauthserivce.Domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserModel> findByEmail(String email);
    @NonNull
    Page<UserModel> findAll(@NonNull Pageable pageable);
}
