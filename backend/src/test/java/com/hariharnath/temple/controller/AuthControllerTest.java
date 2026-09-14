package com.hariharnath.temple.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hariharnath.temple.dto.AuthRequest;
import com.hariharnath.temple.dto.AuthResponse;
import com.hariharnath.temple.dto.UserDto;
import com.hariharnath.temple.entity.Role;
import com.hariharnath.temple.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("POST /api/v1/auth/login - should authenticate and return JWT")
    void testLoginSuccess() throws Exception {
        AuthRequest request = new AuthRequest("admin@hariharnath.in", "Admin@123");

        AuthResponse mockResponse = AuthResponse.builder()
                .token("mocked-jwt-token")
                .type("Bearer")
                .refreshToken("mocked-refresh-token")
                .user(UserDto.builder()
                        .id(1L)
                        .name("Temple Admin")
                        .email("admin@hariharnath.in")
                        .role(Role.ADMIN)
                        .enabled(true)
                        .build())
                .build();

        when(authService.login(any(AuthRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("admin@hariharnath.in"))
                .andExpect(jsonPath("$.user.role").value("ADMIN"));
    }
}
