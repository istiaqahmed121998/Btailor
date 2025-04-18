package com.backend.userauthserivce.domain.profile;


import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository=profileRepository;
    }


    public ProfileModel createProfile(ProfileModel profile) {
        return profileRepository.save(profile);
    }

}
