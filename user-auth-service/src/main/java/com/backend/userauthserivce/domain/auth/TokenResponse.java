package com.backend.userauthserivce.domain.auth;

import java.util.Set;

public record TokenResponse (String accessToken, String tokenType,String refreshToken, Long expiresIn,String email,String name,Set<String> roles){
}
