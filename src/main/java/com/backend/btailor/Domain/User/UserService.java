package com.backend.btailor.Domain.User;

import com.backend.btailor.Domain.Profile.ProfileDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDTO getUserById(Long id) {
        if(userRepository.existsById(id)) {
            UserModel userInfo = userRepository.findById(id).get();
            return convertToDTO(userInfo);
        }
        else{
            throw new RuntimeException("User not found");
        }
    }
    public UserDTO convertToDTO(UserModel user) {
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setName(user.getProfile().getName());
        profileDTO.setEmail(user.getProfile().getEmail());
        profileDTO.setAddress(user.getProfile().getAddress());
        profileDTO.setPhone(user.getProfile().getPhone());
        profileDTO.setCreatedAt(user.getProfile().getCreatedAt());
        profileDTO.setUpdatedAt(user.getProfile().getUpdatedAt());
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profile(profileDTO)
                .build();
    }
    public UserDTO updateUser(Long id,UserModel user) {
        if(userRepository.existsById(id)) {
            UserModel existUser= userRepository.findById(id).get();
            existUser.setUsername(user.getUsername());
            existUser.setPassword(user.getPassword());
            UserModel updatedUser=userRepository.save(existUser);
            return convertToDTO(updatedUser);
        }
        return null;
    }
    public UserDTO partialUpdateUser(Long id,UserModel user) {
        if(userRepository.existsById(id)) {
            UserModel existUser= userRepository.findById(id).get();
            if (user.getUsername()!=null)
                existUser.setUsername(user.getUsername());
            if (user.getPassword()!=null)
                existUser.setPassword(user.getPassword());
            UserModel updatedUser=userRepository.save(existUser);
            return convertToDTO(updatedUser);
        }
        return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Ensure role name matches DB
                .collect(Collectors.toList());

        return user;
    }
}
