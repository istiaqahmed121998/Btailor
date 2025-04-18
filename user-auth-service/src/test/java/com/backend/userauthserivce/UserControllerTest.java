package com.backend.userauthserivce;

import com.backend.userauthserivce.domain.user.UserController;
import com.backend.userauthserivce.domain.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(properties = "spring.cloud.config.enabled=false")
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        // Optional: Set up common mock data or security context here
    }

    @Test
    public void testGetCurrentUserDetails() throws Exception {
        // Simulate authenticated user
        var auth = new TestingAuthenticationToken("istiaq@example.com", "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/user/me")
                        .with((RequestPostProcessor) authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("istiaq@example.com"))
                .andExpect(jsonPath("$.data.roles[0]").value("ROLE_USER"));
    }


    @Test
    public void testGetAllUsersAsNonAdmin_ShouldBeForbidden() throws Exception {
        var auth = new TestingAuthenticationToken("user@example.com", "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/user/")
                        .with((RequestPostProcessor) authentication(auth)))
                .andExpect(status().isForbidden());
    }
}