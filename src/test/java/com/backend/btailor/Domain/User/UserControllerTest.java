package com.backend.btailor.Domain.User;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)  // Enable Mockito annotations
@WebMvcTest(UserController.class)  // WebMvcTest annotation for controller testing
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;  // Mock the service class

    private UserDTO mockUserDTO;
    private UserModel mockUserModel;

    @BeforeEach
    public void setUp() {
        // Mock data for testing
        mockUserDTO = new UserDTO(1L, "john", "ADMIN");
        mockUserModel = new UserModel(1L,"john", "password123", UserModel.Role.USER, null, null, null);
    }

    @Test
    public void testGetUserById() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(mockUserDTO);

        // Act
        ResultActions result = mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    public void testCreateUser() throws Exception {
        // Arrange
        when(userService.createUser(mockUserModel)).thenReturn(mockUserDTO);

        // Act
        ResultActions result = mockMvc.perform(post("/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"zenith\", \"password\": \"password123\", \"role\": \"USER\" }"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Arrange
        when(userService.updateUser(1L, mockUserModel)).thenReturn(mockUserDTO);

        // Act
        ResultActions result = mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"john.doe@example.com\", \"password\": \"password123\", \"role\": \"USER\" }"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testPartiallyUpdateUser() throws Exception {
        // Arrange
        when(userService.partialUpdateUser(1L, mockUserModel)).thenReturn(mockUserDTO);

        // Act
        ResultActions result = mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"john.doe@example.com\" }"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john.doe@example.com"));
    }
}
