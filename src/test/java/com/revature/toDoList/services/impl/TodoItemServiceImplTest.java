package com.revature.toDoList.services.impl;

import com.revature.toDoList.dto.mapper.TodoItemMapper;
import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.response.SubTaskResponse;
import com.revature.toDoList.dto.response.TodoItemResponse;
import com.revature.toDoList.entity.TodoItem;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.repository.TodoItemRepository;
import com.revature.toDoList.repository.UserRepository;
import com.revature.toDoList.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceImplTest {

    @Mock private TodoItemRepository todoItemRepository;
    @Mock private UserRepository userRepository;
    @Mock private TodoItemMapper mapper;

    @InjectMocks
    private TodoItemServiceImpl todoItemService;

    private PasswordEncoder encoder;

    private User user;
    private String username;

    private TodoItemCreateRequest createRequest;
    private TodoItem entity;
    private TodoItem saved;
    private TodoItemResponse response;

    // shared subtasks list for response objects
    private List<SubTaskResponse> subTasks;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();

        username = "user1";

        user = new User();
        user.setUserId("U1");
        user.setUsername(username);
        user.setRole("USER");
        user.setEmail("user1@example.com");
        user.setPassword(encoder.encode("Password123!"));

        createRequest = new TodoItemCreateRequest(
                null,
                "Finish Spring Security",
                "Implement JWT authentication and authorization",
                LocalDate.now().plusDays(2),
                false,
                LocalDateTime.now()
        );

        entity = new TodoItem();
        entity.setTitle("Finish Spring Security");
        entity.setDescription("Implement JWT authentication and authorization");
        entity.setDueDate(createRequest.dueDate());
        entity.setCompleted(false);
        entity.setUser(user);

        saved = new TodoItem();
        saved.setTodoId(1L);
        saved.setTitle(entity.getTitle());
        saved.setDescription(entity.getDescription());
        saved.setDueDate(entity.getDueDate());
        saved.setCompleted(entity.isCompleted());
        saved.setUser(user);

        //for create tests, subtasks can be empty
        subTasks = List.of();

        response = new TodoItemResponse(
                saved.getTodoId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getDueDate(),
                saved.isCompleted(),
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                user.getUserId(),
                subTasks
        );
    }

    @Test
    void createToDoItem_success() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn(username);

            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(mapper.toEntity(createRequest)).thenReturn(entity);
            when(todoItemRepository.save(any(TodoItem.class))).thenReturn(saved);
            when(mapper.toResponse(saved)).thenReturn(response);

            TodoItemResponse actual = todoItemService.createToDoItem(createRequest);

            assertNotNull(actual);
            assertEquals("Finish Spring Security", actual.title());
            assertFalse(actual.completed());
            assertNotNull(actual.subTasks());
            assertEquals(0, actual.subTasks().size());

            verify(userRepository).findByUsername(username);
            verify(todoItemRepository).save(any(TodoItem.class));
            verify(mapper).toResponse(saved);
        }
    }

    @Test
    void createToDoItem_throws_whenUserNotFound() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn(username);

            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> todoItemService.createToDoItem(createRequest));
            verify(userRepository).findByUsername(username);
            verifyNoInteractions(todoItemRepository);
        }
    }

    @Test
    void createToDoItem_setsUser_andCompletedDefaults() {

        TodoItemCreateRequest reqCompletedTrue = new TodoItemCreateRequest(
                null,
                createRequest.title(),
                createRequest.description(),
                createRequest.dueDate(),
                true,
                LocalDateTime.now()
        );

        entity.setCompleted(true);
        saved.setCompleted(true);

        TodoItemResponse responseCompleted = new TodoItemResponse(
                saved.getTodoId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getDueDate(),
                saved.isCompleted(),
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                user.getUserId(),
                List.of() // ✅ empty subtasks list
        );

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn(username);

            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(mapper.toEntity(reqCompletedTrue)).thenReturn(entity);
            when(todoItemRepository.save(any(TodoItem.class))).thenReturn(saved);
            when(mapper.toResponse(saved)).thenReturn(responseCompleted);

            TodoItemResponse actual = todoItemService.createToDoItem(reqCompletedTrue);

            assertTrue(actual.completed());
            assertNotNull(actual.subTasks());
            assertEquals(0, actual.subTasks().size());

            verify(todoItemRepository).save(any(TodoItem.class));
        }
    }
}
