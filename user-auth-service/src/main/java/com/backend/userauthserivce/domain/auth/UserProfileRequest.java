package com.backend.userauthserivce.domain.auth;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserProfileRequest(
        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 15)
        @JsonProperty("name")
        String name,

        @NotBlank(message = "Email is mandatory")
        @Email
        @JsonProperty("email")
        String email,

        @JsonProperty("username")
        String username,

        @JsonProperty("password")
        String password
) {
        // No need for explicit field declarations as record handles it automatically
}
