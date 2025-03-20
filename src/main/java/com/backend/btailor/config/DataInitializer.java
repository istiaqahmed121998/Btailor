package com.backend.btailor.config;

import com.backend.btailor.Domain.Profile.ProfileModel;
import com.backend.btailor.Domain.Role.Role;
import com.backend.btailor.Domain.Role.RoleRepository;
import com.backend.btailor.Domain.User.UserModel;
import com.backend.btailor.Domain.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Create admin user if not present
        if (!userRepository.existsByUsername("admin")) {
            UserModel adminUser = new UserModel();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Use strong password in production
            ProfileModel adminProfile=new ProfileModel();
            adminProfile.setName("admin");
            adminProfile.setPhone("123456789");
            adminUser.setProfile(adminProfile);
            // Assign ROLE_ADMIN
            Set<Role> roles = new HashSet<>();
            roleRepository.findByRoleName("ROLE_ADMIN").ifPresent(roles::add);
            adminUser.setRoles(roles);

            userRepository.save(adminUser);
            System.out.println("Admin user created successfully.");
        }
    }

}