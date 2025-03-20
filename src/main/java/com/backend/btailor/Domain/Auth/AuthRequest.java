package com.backend.btailor.Domain.Auth;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthRequest(@JsonProperty("email") String email, @JsonProperty("password") String password) {

}
