package com.backend.btailor.Domain.User;

import com.backend.btailor.Domain.Profile.ProfileDTO;
public record UserDTO(Long id, String username,String email,ProfileDTO profile) {
}
