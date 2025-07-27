package com.backend.userauthserivce.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpStoreRepository extends JpaRepository<OtpStore, Long> {
}
