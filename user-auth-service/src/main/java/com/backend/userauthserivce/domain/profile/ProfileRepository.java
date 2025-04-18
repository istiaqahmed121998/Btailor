package com.backend.userauthserivce.domain.profile;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProfileRepository extends JpaRepository<ProfileModel, Long> {

}
