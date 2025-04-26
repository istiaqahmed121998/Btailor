package com.backend.userauthserivce.config;

import com.backend.userauthserivce.domain.auth.AuthService;
import com.backend.userauthserivce.domain.auth.TokenResponse;
import com.backend.userauthserivce.domain.user.UserRepository;
import com.backend.userauthserivce.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.backend.userauthserivce.domain.auth.UserProfileRequest;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthService authService;

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil, UserRepository userRepository, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authService = authService;
    }
    public String generateRandomPassword() {
        // Generate a random password
        // Simple random numeric password
        return new Random().nextInt(999999) + "";
    }

    public String hashPassword(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPassword);  // Hash the password
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User user = (DefaultOAuth2User) token.getPrincipal();
        String email = user.getAttribute("email");
        TokenResponse tokenResponse;
        if(userRepository.existsByEmail(email)) {
            try {
                tokenResponse=new TokenResponse(jwtUtil.generateToken(user.getAttribute("id"),user.getAttribute("name"),email,user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())),"Bearer ",null,jwtUtil.getExpirationTime(),user.getAttribute("email"),user.getAttribute("name"), Set.of("ROLE_USER"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                tokenResponse = authService.createUser(new UserProfileRequest(user.getAttribute("name"), user.getAttribute("email"), user.getAttribute("sub"), hashPassword(generateRandomPassword())), "ROLE_USER");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(tokenResponse);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}