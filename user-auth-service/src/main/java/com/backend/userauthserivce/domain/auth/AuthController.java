package com.backend.userauthserivce.domain.auth;

import com.backend.userauthserivce.exception.ValidationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final String  INVALID_USER_DATA="Invalid User Data";

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/user/signup")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody @Valid UserProfileRequest request, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_USER_DATA, bindingResult);
        }

        TokenResponse authResponse = authService.createUser(request, "ROLE_USER");
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/vendor/signup")
    public ResponseEntity<TokenResponse> registerVendor(@RequestBody @Valid UserProfileRequest request, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_USER_DATA, bindingResult);
        }
        TokenResponse authResponse = authService.createUser(request, "ROLE_VENDOR");
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody @Valid AuthRequest authRequest, BindingResult bindingResult) throws AccountLockedException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_USER_DATA, bindingResult);
        }
        TokenResponse token=authService.loginUser(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@RequestParam @NotBlank String accessToken, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_USER_DATA, bindingResult);
        }
        AccessTokenResponse token=authService.refreshAccessToken(accessToken);
        return ResponseEntity.ok(token);
    }
}
