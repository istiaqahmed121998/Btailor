package com.backend.userauthserivce.domain.auth;

public record AccessTokenResponse(String accessToken, String refreshToken) {
}
