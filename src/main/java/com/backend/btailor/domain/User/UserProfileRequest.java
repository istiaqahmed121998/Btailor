package com.backend.btailor.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserProfileRequest {
        @NotBlank(message = "Name is mandatory")
        @Size(min=2, max=15)
        @JsonProperty("name")
        private String name;
        @NotBlank(message = "Email is mandatory")
        @Email
        @JsonProperty("email")
        private String email;
        @JsonProperty("username")
        private String username;
        @JsonProperty("password")
        private String password;
}
