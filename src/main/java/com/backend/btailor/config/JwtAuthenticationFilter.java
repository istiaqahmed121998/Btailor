package com.backend.btailor.config;

import com.backend.btailor.Domain.User.UserService;
import com.backend.btailor.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(email);
                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (ExpiredJwtException ex) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType("application/json");
//
//            Map<String, Object> responseBody = new HashMap<>();
//            responseBody.put("timestamp", System.currentTimeMillis());
//            responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
//            responseBody.put("error", "Unauthorized");
//            responseBody.put("message", "Token expired, please log in again.");
//            responseBody.put("path", request.getRequestURI());
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        } catch (Exception ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("timestamp", LocalDateTime.now().toString());
            responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
            responseBody.put("error", "Unauthorized");
            responseBody.put("message", "Invalid Token");
            responseBody.put("path", request.getRequestURI());

            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        }
        chain.doFilter(request, response);
    }
}