package com.backend.userauthserivce.domain.user;
import java.util.Set;

public record UserResponse(String username,Set<String> roles) {

}
