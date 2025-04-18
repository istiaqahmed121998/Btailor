package com.backend.apigateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class RoleBasedGatewayFilter implements GatewayFilterFactory<RoleBasedGatewayFilter.Config> {

    public static class Config {
        private List<String> roles = new ArrayList<>();
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String token = authHeader.replace("Bearer ", "");
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getPublicKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                List<String> userRoles = claims.get("roles", List.class);
                if (userRoles == null || userRoles.stream().noneMatch(config.roles::contains)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
                return chain.filter(exchange);
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }
    private RSAPublicKey getPublicKey() throws Exception {
        ClassPathResource resource = new ClassPathResource("keys/public.pem");
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePrivate(spec);
    }
}