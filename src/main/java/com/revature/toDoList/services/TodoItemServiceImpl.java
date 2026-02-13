package com.revature.toDoList.services;

import com.revature.toDoList.dto.mapper.TodoItemMapper;
import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.response.TodoItemResponse;
import com.revature.toDoList.entity.TodoItem;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.exception.ToDoListItemNotFoundException;
import com.revature.toDoList.exception.TodoIdNotFoundException;
import com.revature.toDoList.exception.UserNotFoundFoundException;
import com.revature.toDoList.repository.TodoItemRepository;
import com.revature.toDoList.repository.UserRepository;
import com.revature.toDoList.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final TodoItemMapper mapper;
    private final UserRepository userRepository;

    @Override
    public TodoItemResponse createToDoItem(TodoItemCreateRequest todoItem) {

        String username = SecurityUtils.getCurrentUsername();
        log.info("current user {}",username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundFoundException("User not found"));


        TodoItem entity = mapper.toEntity(todoItem);
        entity.setCompleted(false);
        entity.setUser(user);

        TodoItem saved = todoItemRepository.save(entity);

        return mapper.toResponse(saved);
    }


    @Override
    public List<TodoItemResponse> getToDoItemByUserId(String userId) {

       List<TodoItem> todoList = todoItemRepository.findByUser_UserId(userId).orElseThrow(
               () -> new ToDoListItemNotFoundException("No To Do List found "));

        return todoList.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public TodoItemResponse getByTodoId(long todoId) {

            TodoItem item = todoItemRepository.findById(todoId).orElseThrow(
                    () -> new TodoIdNotFoundException("to do id not found"));

        return mapper.toResponse(item);
    }

    @Override
    public void updateTodoItem(long todoId) {

    }

}
