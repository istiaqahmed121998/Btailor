package com.backend.btailor.Domain.Auth;

import com.backend.btailor.exception.ValidationException;
import com.backend.btailor.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final String  INVALID_USER_DATA="Invalid User Data";

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/user/signup")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody @Valid UserProfileRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_USER_DATA, bindingResult);
        }

        TokenResponse authResponse = authService.createUser(request, "ROLE_USER");
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/vendor/signup")
    public ResponseEntity<TokenResponse> registerVendor(@RequestBody @Valid UserProfileRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_USER_DATA, bindingResult);
        }
        TokenResponse authResponse = authService.createUser(request, "ROLE_USER");
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody @Valid AuthRequest authRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_USER_DATA, bindingResult);
        }
        TokenResponse token=authService.loginUser(authRequest);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/google")
    public Map<String, String> authenticateGoogle(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String jwtToken = jwtUtil.generateToken(email);
        return Map.of("token", jwtToken);
    }
}
