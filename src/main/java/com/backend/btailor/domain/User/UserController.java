package com.backend.btailor.domain.User;

import com.backend.btailor.exception.ValidationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
         UserDTO userInformation=userService.getUserById(id);
         return ResponseEntity.ok(userInformation);
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserProfileRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Invalid User Data", bindingResult);
        }
         userService.createUser(request);
        return null;
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

}

