package com.backend.userauthserivce.domain.role;

import com.backend.userauthserivce.domain.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<UserModel, Long> {
    @Query("SELECT r FROM Role r WHERE r.name = :roleName") //
    Optional<Role> findByRoleName(@Param("roleName") String roleName);

}
