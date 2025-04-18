package com.backend.userauthserivce;


import com.backend.userauthserivce.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    public void testGenerateAndValidateToken() throws Exception {
        String email = "istiaq@example.com";

        // 🔐 Generate token
        String token = jwtUtil.generateToken(email,1L, List.of("ROLE_ADMIN"));
        System.out.println("Generated Token: " + token);

        // ✅ Validate token
        assertTrue(jwtUtil.validateToken(token), "Token should be valid");

        // 📧 Extract email
        String extractedEmail = jwtUtil.extractEmail(token);
        assertEquals(email, extractedEmail, "Extracted email should match original");
    }

    @Test
    public void testInvalidTokenShouldFail() {
        String fakeToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.ey...invalid...signature";

        // ❌ Expect validation failure
        assertFalse(jwtUtil.validateToken(fakeToken), "Token should be invalid");
    }

    @Test
    public void testRefreshTokenGeneration() throws Exception {
        String username = "istiaq@example.com";
        String refreshToken = jwtUtil.generateRefreshToken(username);

        assertNotNull(refreshToken, "Refresh token should not be null");
        assertTrue(jwtUtil.validateToken(refreshToken), "Refresh token should be valid");

        String subject = jwtUtil.extractEmail(refreshToken);
        assertEquals(username, subject, "Refresh token subject should match username");
    }
}
