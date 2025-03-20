package com.backend.btailor.Domain.User;
import java.util.Set;

public record UserResponse(String username,Set<String> roles) {

}
