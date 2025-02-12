package com.backend.btailor.domain.User;

import com.backend.btailor.domain.Profile.ProfileDTO;
import com.backend.btailor.domain.Profile.ProfileModel;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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

    public UserDTO createUser(UserProfileRequest request) {
        UserModel user = new UserModel();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(UserModel.Role.valueOf("ADMIN"));
        ProfileModel profile = new ProfileModel();
        profile.setName(request.getName());
        profile.setEmail(request.getEmail());
        profile.setEmail(request.getEmail());
        user.setProfile(profile);
        UserModel userinfo= userRepository.save(user);
        if(userinfo != null) {
           return convertToDTO(userinfo);
        }else{
           new RuntimeException("User is not created");
        }
        return null;
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
                .role(user.getRole().name())
                .profile(profileDTO)
                .build();
    }

    public UserDTO updateUser(Long id,UserModel user) {
        if(userRepository.existsById(id)) {
            UserModel existUser= userRepository.findById(id).get();
            existUser.setUsername(user.getUsername());
            existUser.setRole(user.getRole());
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
            if (user.getRole()!=null)
                existUser.setRole(user.getRole());
            if (user.getPassword()!=null)
                existUser.setPassword(user.getPassword());
            UserModel updatedUser=userRepository.save(existUser);
            return convertToDTO(updatedUser);
        }
        return null;
    }
}
