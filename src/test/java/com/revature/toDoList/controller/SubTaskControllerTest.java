package com.revature.toDoList.controller;

import com.revature.toDoList.auth.JwtService;
import com.revature.toDoList.dto.request.SubTaskCreateRequest;
import com.revature.toDoList.entity.SubTask;
import com.revature.toDoList.entity.TodoItem;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.repository.SubTaskRepository;
import com.revature.toDoList.repository.TodoItemRepository;
import com.revature.toDoList.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class SubTaskControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired UserRepository userRepository;
    @Autowired TodoItemRepository todoItemRepository;
    @Autowired SubTaskRepository subTaskRepository;

    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtService jwtService;

    private String tokenUser;
    private long todoId;
    private long subId;

    @BeforeEach
    void setUp() {
        // User
        User user = new User();
        user.setUsername("user1");
        user.setRole("USER");
        user.setEmail("user1@example.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        User savedUser = userRepository.save(user);

        tokenUser = jwtService.generateToken(savedUser);

        // TodoItem (required for FK in SubTask)
        TodoItem todo = new TodoItem();
        todo.setTitle("Todo Parent");
        todo.setDescription("Parent todo for subtask tests");
        todo.setDueDate(LocalDate.now().plusDays(2));
        todo.setCompleted(false);
        todo.setUpdatedAt(LocalDateTime.now());
        todo.setUser(savedUser);

        TodoItem savedTodo = todoItemRepository.save(todo);
        todoId = savedTodo.getTodoId();

        // SubTask (seed one record for GET/PUT/DELETE)
        SubTask sub = new SubTask();
        sub.setDescription("Initial Subtask");
        sub.setCompleted(false);
        sub.setTodoItem(savedTodo);

        SubTask savedSub = subTaskRepository.save(sub);
        subId = savedSub.getSubTaskId();
    }

    @Test
    void createSubTask() throws Exception {
        SubTaskCreateRequest req = new SubTaskCreateRequest(
                "New Subtask Description",
                false
        );

        mockMvc.perform(post("/api/v1/user/task/add/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("New Subtask Description"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.todoId").value((int) todoId));
    }

    @Test
    void updateSubTask() throws Exception {
        SubTaskCreateRequest req = new SubTaskCreateRequest(
                "Updated Subtask Description",
                true
        );

        mockMvc.perform(put("/api/v1/user/task/update/{subId}", subId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subTaskId").value((int) subId))
                .andExpect(jsonPath("$.description").value("Updated Subtask Description"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.todoId").value((int) todoId));
    }

    @Test
    void getSubTaskById() throws Exception {
        // Your controller MUST bind the path variable correctly (subId -> param)
        mockMvc.perform(get("/api/v1/user/task/{subId}", subId)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subTaskId").value((int) subId))
                .andExpect(jsonPath("$.description").value("Initial Subtask"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.todoId").value((int) todoId));
    }

    @Test
    void getAllSubTaskByTodoItemId() throws Exception {
        mockMvc.perform(get("/api/v1/user/task/{todoId}/items", todoId)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subTaskId").value((int) subId))
                .andExpect(jsonPath("$[0].description").value("Initial Subtask"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[0].todoId").value((int) todoId));
    }

    @Test
    void deleteSubTask() throws Exception {
        mockMvc.perform(delete("/api/v1/user/task/delete/{subId}", subId)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(content().string("Subtask successfully deleted"));
    }
}
