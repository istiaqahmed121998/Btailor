package com.backend.btailor.Domain.User;

import com.backend.btailor.Domain.Profile.ProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO) // Converts if user exists
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public UserDTO convertToDTO(UserModel user) {
        ProfileDTO profileDTO=new ProfileDTO(user.getProfile().getName(),user.getProfile().getAddress(),user.getProfile().getPhone(),user.getProfile().getCreatedAt(),user.getProfile().getUpdatedAt());
        return new UserDTO(user.getId(),user.getUsername(),user.getEmail(),profileDTO);
    }
    public UserDTO updateUser(Long id,UserModel user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword(user.getPassword());
                    UserModel updatedUser = userRepository.save(existingUser);
                    return convertToDTO(updatedUser);
                })
                .orElse(null);
    }
    public UserDTO partialUpdateUser(Long id,UserModel user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (user.getUsername()!=null)
                        existingUser.setUsername(user.getUsername());
                    if (user.getPassword()!=null)
                        existingUser.setPassword(user.getPassword());
                    UserModel updatedUser=userRepository.save(existingUser);
                    return convertToDTO(updatedUser);
                })
                .orElse(null);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<UserModel> userPage = userRepository.findAll(pageable);
        return userPage.map(this::convertToDTO);

    }
}
