package com.backend.userauthserivce.domain.user;

import com.backend.userauthserivce.domain.profile.ProfileDTO;
public record UserDTO(Long id, String username,String email,ProfileDTO profile) {
}
