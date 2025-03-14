package com.backend.btailor.Domain.Role;

import com.backend.btailor.Domain.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
public interface RoleRepository extends JpaRepository<UserModel, Long> {
    @Query("SELECT r FROM Role r WHERE r.name = :roleName") //
    Optional<Role> findByRoleName(@Param("roleName") String roleName);

}
