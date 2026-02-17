package com.revature.toDoList.controller;

import com.revature.toDoList.auth.JwtService;
import com.revature.toDoList.dto.request.AuthRequest;
import com.revature.toDoList.dto.request.RegisterRequest;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtService jwtService;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenUser;

    private String userId;

    @BeforeEach
    void setUp() {
        User admin = new User();
        admin.setUsername("admin_" + java.util.UUID.randomUUID());
        admin.setRole("ADMIN");
        admin.setEmail("admin_" + java.util.UUID.randomUUID() + "@example.com");
        admin.setPassword(passwordEncoder.encode("Password123!"));
        User adminSaved = userRepository.save(admin);

        User user = new User();
        user.setUsername("user1");
        user.setRole("USER");
        user.setEmail("user1@example.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        User userSaved = userRepository.save(user);

        tokenAdmin = jwtService.generateToken(adminSaved);
        tokenUser = jwtService.generateToken(userSaved);

      userId = jwtService.extractUserId(tokenUser);
    }

    @Test
    void testGetAllUsers_ok_by_admin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers_403_by_user() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users")
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/v1/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));

    }




}
