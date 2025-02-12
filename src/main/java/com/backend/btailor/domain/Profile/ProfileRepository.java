package com.backend.btailor.domain.Profile;

import com.backend.btailor.domain.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProfileRepository extends JpaRepository<ProfileModel, Long> {

}
