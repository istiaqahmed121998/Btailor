package com.backend.userauthserivce.Domain.User;

import com.backend.userauthserivce.Domain.Profile.ProfileDTO;
public record UserDTO(Long id, String username,String email,ProfileDTO profile) {
}
