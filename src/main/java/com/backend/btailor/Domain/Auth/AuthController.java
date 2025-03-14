package com.backend.btailor.Domain.Auth;

import com.backend.btailor.exception.ValidationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/user/signup")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody @Valid UserProfileRequest request, BindingResult bindingResult) {
        System.out.println("registerUser");
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Invalid User Data", bindingResult);
        }

        TokenResponse authResponse = authService.createUser(request, "ROLE_USER");
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/vendor/signup")
    public ResponseEntity<TokenResponse> registerVendor(@RequestBody @Valid UserProfileRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Invalid User Data", bindingResult);
        }
        TokenResponse authResponse = authService.createUser(request, "ROLE_USER");
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody @Valid AuthRequest authRequest, BindingResult bindingResult) {
        System.out.println("loginUser");
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Invalid User Data", bindingResult);
        }
        TokenResponse token=authService.loginUser(authRequest);
        return ResponseEntity.ok(token);
    }
}
