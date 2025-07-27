package com.backend.userauthserivce.config;

import com.backend.userauthserivce.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    public JwtUtil jwtUtil() throws Exception {
        RSAPrivateKey privateKey = loadPrivateKeyFromFile();
        RSAPublicKey publicKey = loadPublicKeyFromFile();
        return new JwtUtil(publicKey,privateKey);
    }
    @Bean
    public RSAPrivateKey loadPrivateKeyFromFile() throws Exception {
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
    @Bean
    public RSAPublicKey loadPublicKeyFromFile() throws Exception {
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
}
