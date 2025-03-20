package com.backend.btailor.Domain.Auth;

import com.backend.btailor.Domain.Profile.ProfileModel;
import com.backend.btailor.Domain.Role.Role;
import com.backend.btailor.Domain.Role.RoleRepository;
import com.backend.btailor.Domain.User.*;
import com.backend.btailor.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    Logger logger = Logger.getLogger(getClass().getName());
    public AuthService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository, JwtUtil jwtUtil, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }
    public TokenResponse loginUser(@Valid AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
        );
        if (authentication.isAuthenticated()) {
            UserModel user = userRepository.findByEmail(authRequest.email())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Set<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            return new TokenResponse(jwtUtil.generateToken(authRequest.email()),"Bearer ",null,jwtUtil.getExpirationTime(),authRequest.email(),user.getProfile().getName(),roles);

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
    public TokenResponse createUser(UserProfileRequest request, String roleName) {
        UserModel user = new UserModel();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        Set<Role> roles = new HashSet<>();
        roleRepository.findByRoleName("ROLE_USER").ifPresentOrElse(
                roles::add,
                () -> logger.info("ROLE_USER not found in DB")
        );
        if (roleName.equals("ROLE_VENDOR")) {
            roleRepository.findByRoleName("ROLE_VENDOR").ifPresentOrElse(
                    roles::add,
                    () -> logger.info("ROLE_VENDOR not found in DB")
            );
        }
        user.setRoles(roles);
        ProfileModel profile = new ProfileModel();
        profile.setName(request.name());

        user.setProfile(profile);
        userRepository.save(user);
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new TokenResponse(jwtUtil.generateToken(user.getEmail()),"Bearer ",null,jwtUtil.getExpirationTime(),user.getUsername(),request.name(),roleNames);

    }
}
