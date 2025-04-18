package com.backend.userauthserivce.domain.auth;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthRequest(@JsonProperty("email") String email, @JsonProperty("password") String password) {
}
