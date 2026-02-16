package com.revature.toDoList.controller;

import com.revature.toDoList.dto.request.AuthRequest;
import com.revature.toDoList.dto.request.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final String username = "John";
    private final String password = "password1234";
    private final String email = "test@example.com";
    private final String role = "USER";

    @BeforeEach
    void setUp() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setRole(role);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    void registerNewUserTest() throws Exception{

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("James");
        registerRequest.setEmail("james@example.com");
        registerRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String username = json.get("username").asString();
        String email = json.get("email").asString();
        String role = json.get("role").asString();
        String id = json.get("userId").asString();

        Assertions.assertEquals(registerRequest.getUsername(),username);
        Assertions.assertEquals(registerRequest.getEmail(),email);
        Assertions.assertEquals("USER",role);
        Assertions.assertNotNull(id);

    }

    @Test
    void authenticate_returnToken() throws Exception {

        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setPassword(password);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        String token = node.get("accessToken").asString();
        Assertions.assertTrue(token.startsWith("ey"));

    }


}