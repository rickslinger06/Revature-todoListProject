package com.revature.toDoList.services.impl;

import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.repository.TodoItemRepository;
import com.revature.toDoList.services.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TodoItemServiceImplTest {
    @Mock
    private TodoItemRepository todoItemRepository;

    @InjectMocks
    private TodoItemServiceImpl todoItemService;
    @Autowired
    private PasswordEncoder encoder;

    private User user;

    @BeforeEach
    void setUp() {

       user = new User();
        user.setUsername("user1");
        user.setRole("USER");
        user.setEmail("user1@example.com");
        user.setPassword(encoder.encode("Password123!"));

    }

    @Test
    void createToDoItem() {
        TodoItemCreateRequest request = new TodoItemCreateRequest(
                null,
                "Finish Spring Security",
                "Implement JWT authentication and authorization",
                LocalDate.now().plusDays(2),
                false,
                LocalDateTime.now()
        );


    }

    @Test
    void getToDoItemByUserId() {
    }

    @Test
    void getByTodoId() {
    }

    @Test
    void updateTodoItem() {
    }

    @Test
    void deleteToDoItem() {
    }
}