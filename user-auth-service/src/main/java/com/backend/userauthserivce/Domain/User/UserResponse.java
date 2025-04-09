package com.backend.userauthserivce.Domain.User;
import java.util.Set;

public record UserResponse(String username,Set<String> roles) {

}
