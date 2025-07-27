package com.backend.userauthserivce.domain.auth;

import com.backend.common.events.OtpGeneratedEvent;
import com.backend.common.events.UserCreatedEvent;
import com.backend.userauthserivce.domain.profile.ProfileModel;
import com.backend.userauthserivce.domain.role.Role;
import com.backend.userauthserivce.domain.role.RoleRepository;
import com.backend.userauthserivce.domain.user.UserModel;
import com.backend.userauthserivce.domain.user.UserRepository;
import com.backend.userauthserivce.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
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
    private final LoginAttemptService loginAttemptService;
    private final UserEventPublisher userEventPublisher;
    private final SecureRandom secureRandom = new SecureRandom();
    private final OtpStoreRepository otpStoreRepository;
    private final ForgetPasswordPublisher forgetPasswordPublisher;
    Logger logger = Logger.getLogger(getClass().getName());
    public AuthService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository, JwtUtil jwtUtil, UserRepository userRepository, LoginAttemptService loginAttemptService, UserEventPublisher userEventPublisher, OtpStoreRepository otpStoreRepository, ForgetPasswordPublisher forgetPasswordPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
        this.userEventPublisher = userEventPublisher;
        this.otpStoreRepository = otpStoreRepository;
        this.forgetPasswordPublisher = forgetPasswordPublisher;
    }
    public TokenResponse loginUser(@Valid AuthRequest authRequest) throws AccountLockedException, AuthenticationException {
        // Check if the account is locked
        if (loginAttemptService.isAccountLocked(authRequest.email())) {
            throw new AccountLockedException("Your account is locked due to multiple failed login attempts.");
        }
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
            );
            if (authentication.isAuthenticated()) {

                UserModel user = userRepository.findByEmail(authRequest.email())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                // Reset failed attempts on successful login
                loginAttemptService.resetFailedAttempts(authRequest.email());

                Set<String> roles = user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet());
                return new TokenResponse(jwtUtil.generateToken(user.getId(),user.getProfile().getName(),authRequest.email(),user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())),"Bearer ",jwtUtil.generateRefreshToken(authRequest.email()),jwtUtil.getExpirationTime(),authRequest.email(),user.getProfile().getName(),roles);
            }
            else {
                throw new UsernameNotFoundException("Invalid user request!");
            }
        } catch (AuthenticationException e) {
            loginAttemptService.incrementFailedAttempts(authRequest.email());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public TokenResponse createUser(UserProfileRequest request, String roleName) throws Exception {
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
        UserCreatedEvent event = new UserCreatedEvent(
                user.getId(), user.getProfile().getName(), user.getEmail()
        );
        userEventPublisher.publishUserCreated(event);
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new TokenResponse(jwtUtil.generateToken(user.getId(),user.getProfile().getName(), user.getEmail(), user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList())),"Bearer ",jwtUtil.generateRefreshToken(user.getEmail()),jwtUtil.getExpirationTime(),user.getUsername(),request.name(),roleNames);
    }

    public AccessTokenResponse refreshAccessToken(@Valid String refreshToken) throws Exception {
        String email = jwtUtil.extractEmail(refreshToken);
        UserModel userModel =  userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        else {
            return new AccessTokenResponse(jwtUtil.generateToken(userModel.getId(),userModel.getProfile().getName(),email,userModel.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())),jwtUtil.generateRefreshToken(email));
        }
    }

    public boolean initiatePasswordReset(String email) {

        Optional<UserModel> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            String otpCode = String.valueOf(100000 + secureRandom.nextInt(900000)); // 6-digit OTP

            OtpStore otpStore = new OtpStore();
            otpStore.setUser(user);
            otpStore.setOtpCode(otpCode);
            otpStore.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES)); // 10-minute expiry

            otpStoreRepository.save(otpStore);
            OtpGeneratedEvent otpGeneratedEvent = new OtpGeneratedEvent(user.getId(),user.getEmail(),otpCode);
            forgetPasswordPublisher.publishOtp(otpGeneratedEvent);
            // --- Send Email ---
            // SimpleMailMessage message = new SimpleMailMessage();
            // message.setTo(email);
            // message.setSubject("Your Password Reset Code");
            // message.setText();
            // mailSender.send(message);
            return true;
        }
        return false;
    }
}
