package com.backend.btailor.Domain.Auth;

import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TokenResponse {
    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer"; // Standard practice
    private String refreshToken;
    private Long expiresIn;
    private String username;
    private Set<String> roles;
}
