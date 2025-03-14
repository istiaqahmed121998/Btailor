package com.backend.btailor.Domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByUsername(String username);
    Optional<UserModel> findByUsername(String username);
}
