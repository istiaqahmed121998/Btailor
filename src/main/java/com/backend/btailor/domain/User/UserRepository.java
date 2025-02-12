package com.backend.btailor.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<UserModel, Long> {

}
