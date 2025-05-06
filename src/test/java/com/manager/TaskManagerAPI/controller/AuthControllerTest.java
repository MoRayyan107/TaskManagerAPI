package com.manager.TaskManagerAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.TaskManagerAPI.Util.JwtUtil;
import com.manager.TaskManagerAPI.model.AuthenticationRequest;
import com.manager.TaskManagerAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.manager.TaskManagerAPI.constants.AppConstants.ROLE_USER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    void setup() {
        authenticationRequest = new AuthenticationRequest("johndoe", "secret123");
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        UserDetails userDetails = new User(
                "johndoe", 
                "secret123", 
                List.of(() -> ROLE_USER)
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("johndoe",null, List.of(() -> ROLE_USER)));
        when(userDetailsService.loadUserByUsername("johndoe")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mock-jwt-token");

        String requestJson = objectMapper.writeValueAsString(authenticationRequest);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.jwt").value("mock-jwt-token"));
    }

    @Test
    void testLoginFailure_InvalidCredentials() throws Exception {
        // Arrange
        doThrow(new BadCredentialsException("Incorrect username or password"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        String requestJson = objectMapper.writeValueAsString(authenticationRequest);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterSuccess() throws Exception {
        // Arrange
        doReturn(java.util.Optional.empty())
                .when(userRepository).findByUsername("johndoe");

        String requestJson = objectMapper.writeValueAsString(authenticationRequest);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User registered successfully"));
    }

    @Test
    void testRegisterFailure_UserAlreadyExists() throws Exception {
        // Arrange
        doReturn(java.util.Optional.of(new com.manager.TaskManagerAPI.model.AppUser()))
                .when(userRepository).findByUsername("johndoe");

        String requestJson = objectMapper.writeValueAsString(authenticationRequest);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}