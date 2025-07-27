package com.backend.userauthserivce.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserModel> findByEmail(String email);
    @NonNull
    Page<UserModel> findAll(@NonNull Pageable pageable);
}
