package com.backend.btailor.Domain.Auth;

import java.util.Set;

public record TokenResponse (String accessToken, String tokenType,String refreshToken, Long expiresIn,String email,String name,Set<String> roles){
}
