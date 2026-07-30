package com.fastfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.dto.request.LoginRequest;
import com.fastfood.entity.system.Role;
import com.fastfood.entity.system.User;
import com.fastfood.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    UserRepository userRepository;

    @Test
    void loginSuccess() throws Exception {

        Role role = new Role();
        role.setRoleName("ADMIN");

        User user = new User();
        user.setUsername("admin");
        user.setPasswordHash("123456");
        user.setFullName("Admin");
        user.setRole(role);

        when(userRepository.findByUsername("admin"))
                .thenReturn(user);

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    void loginFail() throws Exception {

        when(userRepository.findByUsername(anyString()))
                .thenReturn(null);

        LoginRequest request = new LoginRequest();
        request.setUsername("abc");
        request.setPassword("123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout() throws Exception {

        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());
    }
}