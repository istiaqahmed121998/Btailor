package com.backend.userauthserivce.Domain.User;

import com.backend.userauthserivce.utils.ApiResponse;
import com.backend.userauthserivce.utils.PaginatedResponse;
import com.backend.userauthserivce.utils.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
// UserController handles HTTP requests related to User operations
public class UserController {

    // Injecting the UserService to perform business logic
    UserService userService;

    // Constructor injecting dependencies
    public UserController(UserService userService) {
        this.userService = userService;  // Assigning the injected userService to the class-level variable
    }

    // Endpoint to get the details of the currently authenticated user
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserDetails() {

        // Get the current authentication (logged-in user) from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Retrieve the username of the currently authenticated user
        String username = authentication.getName();

        // Retrieve the roles assigned to the currently authenticated user
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // Convert each authority to a role string
                .collect(Collectors.toSet());  // Collect all roles into a Set

        // Return the response with the user details (username and roles) in a success response
        return ResponseUtil.success(new UserResponse(username, roles), HttpStatus.OK.getReasonPhrase());
    }

    // Endpoint to get all users (only accessible by users with 'ADMIN' role)
    @PreAuthorize("hasRole('ADMIN')")  // This annotation ensures only users with 'ADMIN' role can access this endpoint
    @GetMapping("/")
    public ResponseEntity<PaginatedResponse<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,  // Default page is 0
            @RequestParam(defaultValue = "10") int size) {  // Default size is 10
        Pageable pageable = PageRequest.of(page, size);  // Create Pageable object for pagination
        Page<UserDTO> userPage = userService.getAllUsers(pageable);  // Get paginated users from service
        return ResponseUtil.paginated(userPage, HttpStatus.OK.getReasonPhrase());  // Return paginated result
    }

    // Endpoint to get a specific user by ID (only accessible by 'ADMIN' or the user themselves)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")  // Only 'ADMIN' or the user themselves can access this
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        // Fetch user details using the user ID
        UserDTO userInformation = userService.getUserById(id);

        // Return the user information in a success response
        return ResponseUtil.success(userInformation, HttpStatus.OK.getReasonPhrase());
    }

    // Endpoint to update a specific user (only accessible by 'ADMIN' or the user themselves)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")  // Only 'ADMIN' or the user themselves can access this
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserModel user) {
        // Update the user information using the provided user data
        UserDTO userInformation = userService.updateUser(id, user);

        // Return the updated user information in a success response
        return ResponseUtil.success(userInformation, HttpStatus.OK.getReasonPhrase());
    }

    // Endpoint to partially update a specific user (only accessible by 'ADMIN' or the user themselves)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")  // Only 'ADMIN' or the user themselves can access this
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> partiallyUpdateUser(@PathVariable Long id, @RequestBody UserModel user) {
        // Partially update the user information using the provided user data
        UserDTO userInformation = userService.partialUpdateUser(id, user);

        // Return the partially updated user information in a success response
        return ResponseUtil.success(userInformation, HttpStatus.OK.getReasonPhrase());
    }
}
