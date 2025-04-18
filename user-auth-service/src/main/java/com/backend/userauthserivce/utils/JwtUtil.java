package com.backend.userauthserivce.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtUtil {
    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 15; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24; // 24 hours

    private RSAPrivateKey getPrivateKey() throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource("keys/private.pem");
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

    private RSAPublicKey getPublicKey() throws Exception {
        ClassPathResource resource = new ClassPathResource("keys/public.pem");
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

    public String generateToken(String email,Long id,List<String> roles) throws Exception {
        return Jwts.builder()
                .claim("roles", roles)
                .claim("id", id)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String extractEmail(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String generateRefreshToken(String username) throws Exception {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Long getExpirationTime() {
        return ACCESS_TOKEN_EXPIRY / 1000; // Return in seconds
    }

}
