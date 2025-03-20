package com.backend.btailor.config;

import com.backend.btailor.Domain.Auth.AuthService;
import com.backend.btailor.Domain.Auth.TokenResponse;
import com.backend.btailor.Domain.User.UserRepository;
import com.backend.btailor.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.backend.btailor.Domain.Auth.UserProfileRequest;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

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
            tokenResponse=new TokenResponse(jwtUtil.generateToken(email),"Bearer ",null,jwtUtil.getExpirationTime(),user.getAttribute("email"),user.getAttribute("name"), Set.of("ROLE_USER"));
        }
        else {
            tokenResponse = authService.createUser(new UserProfileRequest(user.getAttribute("name"), user.getAttribute("email"), user.getAttribute("sub"), hashPassword(generateRandomPassword())), "ROLE_USER");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(tokenResponse);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}