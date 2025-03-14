package com.backend.btailor.Domain.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(new UserResponse(username, roles));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
         UserDTO userInformation=userService.getUserById(id);
         return ResponseEntity.ok(userInformation);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserModel user) {
        UserDTO userInformation = userService.updateUser(id,user);
        return ResponseEntity.ok(userInformation);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> partiallyUpdateUser(@PathVariable Long id, @RequestBody UserModel user) {
        UserDTO userInformation = userService.partialUpdateUser(id,user);
        return ResponseEntity.ok(userInformation);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user-profile")
    public String userProfile() {
        return "User Profile Accessed!";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-panel")
    public String adminPanel() {
        return "Admin Panel Accessed!";
    }
    @PreAuthorize("hasRole('VENDOR')")
    @GetMapping("/vendor-dashboard")
    public String vendorDashboard() {
        return "Vendor Dashboard Accessed!";
    }
}

