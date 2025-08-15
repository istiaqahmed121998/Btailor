package com.backend.productservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
//    private final HttpServletRequest request;
//
//    public FeignConfig(HttpServletRequest request) {
//        this.request = request;
//    }
//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return template -> {
//            var authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
////                String token = jwtAuth.getToken().getTokenValue();
//                String token = request.getHeader(HttpHeaders.AUTHORIZATION);
//                template.header("Authorization", "Bearer " + token);
//            }
//        };
//    }
}