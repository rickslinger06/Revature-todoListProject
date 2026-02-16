package com.revature.toDoList.controller;

import com.revature.toDoList.auth.JwtService;
import com.revature.toDoList.dto.mapper.TodoItemMapper;
import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.response.TodoItemResponse;
import com.revature.toDoList.entity.TodoItem;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.repository.TodoItemRepository;
import com.revature.toDoList.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class TodoItemControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    @Autowired
    ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenUser;
    private String userId;
    @Autowired
    private TodoItemRepository todoItemRepository;
    @Autowired
    private TodoItemMapper todoItemMapper;

    private long todoId;


    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("user1");
        user.setRole("USER");
        user.setEmail("user1@example.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        User userSaved = userRepository.save(user);


        tokenUser = jwtService.generateToken(userSaved);

        TodoItemCreateRequest request = new TodoItemCreateRequest(
                null,
                "Finish Spring Security",
                "Implement JWT authentication and authorization",
                LocalDate.now().plusDays(2),
                false,
                LocalDateTime.now()
        );


        TodoItem entity = todoItemMapper.toEntity(request);
        entity.setCompleted(Boolean.TRUE.equals(request.completed()));
        entity.setUser(user);

       TodoItem todoItemResponse = todoItemRepository.save(entity);
        userId = jwtService.extractUserId(tokenUser);
        todoId = todoItemResponse.getTodoId();

    }

    @Test
    void createTodoListItem() throws Exception {

        User user = new User();
        user.setUsername("user1");
        user.setRole("USER");
        user.setEmail("user1@example.com");
        user.setPassword(passwordEncoder.encode("Password123!"));

        TodoItemCreateRequest request = new TodoItemCreateRequest(
                null,
                "angular",
                "Implement component",
                LocalDate.now().plusDays(2),
                false,
                LocalDateTime.now()
        );

        MvcResult result = mockMvc.perform(post("/api/v1/user/add-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String title = json.get("title").asText();
        String description = json.get("description").asString();

        Assertions.assertEquals(title, "angular");
        Assertions.assertEquals(description, "Implement component");
        Assertions.assertNotNull(json);

    }

    @Test
    void getAllItemsByUserId() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/v1/user/items/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String title = json.get(0).get("title").asText();

        Assertions.assertEquals(title, "Finish Spring Security");
        Assertions.assertEquals(1, json.size());
        Assertions.assertEquals(userId, json.get(0).get("userId").asString());


    }

    @Test
    void getToDoItemById() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/v1/user/item/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String title = json.get("title").asText();

        Assertions.assertEquals(title, "Finish Spring Security");

    }
//
//    @Test
//    void updateToDoItem() {
//    }
//
    @Test
    void testDeleteToDoItem() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/v1/user/item/delete/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        Assertions.assertEquals("Successfully deleted item " + todoId, body);


    }
}