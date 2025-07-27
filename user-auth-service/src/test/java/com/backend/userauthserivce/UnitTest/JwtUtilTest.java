package com.backend.userauthserivce.UnitTest;


import com.backend.userauthserivce.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() throws Exception {
        RSAPrivateKey privateKey = loadPrivateKeyFromFile("keys/private.pem");
        RSAPublicKey publicKey = loadPublicKeyFromFile("keys/public.pem");

        jwtUtil = new JwtUtil( publicKey,privateKey);
    }

    @Test
    public void testGenerateAndValidateToken() throws Exception {
        String email = "istiaq@example.com";

        // 🔐 Generate token
        String token = jwtUtil.generateToken(1L,"Istiaq",email, List.of("ROLE_ADMIN"));
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

    public RSAPrivateKey loadPrivateKeyFromFile(String path) throws Exception {
        try {
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource(path);
            String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }
    public RSAPublicKey loadPublicKeyFromFile(String path) throws Exception {
        org.springframework.core.io.ClassPathResource resource = new ClassPathResource(path);
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // Remove PEM headers and footers, and any whitespace
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        // Decode the Base64-encoded key
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

        // Generate the public key
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec); // Corrected method call
    }
}
