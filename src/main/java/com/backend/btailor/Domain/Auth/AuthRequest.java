package com.backend.btailor.Domain.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
