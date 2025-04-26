package com.backend.cartservice.domain.service;

import com.backend.cartservice.domain.model.UserInformation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    public UserInformation profile(@AuthenticationPrincipal Jwt jwt) {
        return new UserInformation(jwt.getClaimAsString("id"),
                jwt.getClaimAsString("name"),
             jwt.getSubject());
    }
}
