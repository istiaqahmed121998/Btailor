package com.backend.userauthserivce.domain.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProfileRepository extends JpaRepository<ProfileModel, Long> {

}
