package com.backend.userauthserivce.config;

import com.backend.userauthserivce.domain.auth.AuthService;
import com.backend.userauthserivce.domain.user.UserRepository;
import com.backend.userauthserivce.utils.JwtUtil;
import com.backend.userauthserivce.domain.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    public SecurityConfig(JwtUtil jwtUtil, UserService userService, UserRepository userRepository, @Lazy AuthService authService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService; // Inject UserService directly
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5113")); // Adjust as needed
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Allow CORS if needed
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/api/auth/**","/api/auth/google","/actuator/health").permitAll() // Allow auth endpoints
                        .anyRequest().authenticated() // Require auth for other endpoints
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(new OidcUserService()))
                        .successHandler(oAuth2LoginSuccessHandler()) // Custom handler to generate JWT
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // Custom 401 response
                        .accessDeniedHandler((request, response, accessDeniedException) -> { // Forward 403 to GlobalExceptionHandler
                            throw new AccessDeniedException("You do not have permission to access this resource.");
                        })
                )
                .authenticationProvider(authenticationProvider()) // Ensure custom auth provider is used
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(jwtUtil,userRepository,authService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.FORBIDDEN.value());
            body.put("error", "Forbidden");
            body.put("message", "You do not have permission to access this resource.");
            body.put("timestamp", LocalDateTime.now().toString());
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(body));
        };
    }
}