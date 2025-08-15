package com.backend.userauthserivce.domain.auth;

public record ResetPasswordRequest(String email,String otp,String password,String confirmPassword) {
}
