package com.backend.userauthserivce.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtUtil {
    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 60; // 60 minutes
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 3; // 3 days

    private final RSAPublicKey rsaPublicKey;
    private final RSAPrivateKey rsaPrivateKey;

    public JwtUtil(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        this.rsaPublicKey = rsaPublicKey;
        this.rsaPrivateKey = rsaPrivateKey;
    }


    public String generateToken(Long id,String name,String email,List<String> roles) throws Exception {
        return Jwts.builder()
                .claim("id", id)
                .claim("name", name )
                .claim("roles", roles)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(rsaPrivateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String extractEmail(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(rsaPublicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(rsaPublicKey)
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
                .signWith(rsaPrivateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Long getExpirationTime() {
        return ACCESS_TOKEN_EXPIRY / 1000; // Return in seconds
    }

}
