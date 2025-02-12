package com.backend.btailor.domain.User;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDTO getUserById(Long id) {
        if(userRepository.existsById(id)) {
            return convertToDTO(userRepository.findById(id).get());
        }
        else{
            new RuntimeException("User not found");
        }
        return null;
    }

    public UserDTO createUser(UserModel user) {
       UserModel userinfo= userRepository.save(user);
       if(userinfo != null) {
           return convertToDTO(userinfo);
       }else{
           new RuntimeException("User is not created");
       }
       return null;
    }
    public UserDTO convertToDTO(UserModel user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())  // Convert Enum to String
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
