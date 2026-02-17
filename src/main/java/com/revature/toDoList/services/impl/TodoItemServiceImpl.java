package com.revature.toDoList.services.impl;

import com.revature.toDoList.dto.mapper.TodoItemMapper;
import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.request.TodoUpdateRequest;
import com.revature.toDoList.dto.response.TodoItemResponse;
import com.revature.toDoList.entity.SubTask;
import com.revature.toDoList.entity.TodoItem;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.exception.SubTaskNotClosedException;
import com.revature.toDoList.exception.ToDoListItemNotFoundException;
import com.revature.toDoList.exception.TodoIdNotFoundException;
import com.revature.toDoList.exception.UserNotFoundFoundException;
import com.revature.toDoList.repository.TodoItemRepository;
import com.revature.toDoList.repository.UserRepository;
import com.revature.toDoList.services.TodoItemService;
import com.revature.toDoList.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        log.info("current user {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundFoundException("User not found"));


        TodoItem entity = mapper.toEntity(todoItem);
        entity.setCompleted(Boolean.TRUE.equals(todoItem.completed()));
        entity.setClosed(false);
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
    public TodoItemResponse updateTodoItem(TodoUpdateRequest req) {

        TodoItem item = todoItemRepository.findById(req.getId()).orElseThrow(
                () -> new TodoIdNotFoundException("No to do item found"));

        log.info("To do ID FOUND" + req.getId());

        item.setTitle(req.getTitle());
        item.setCompleted(req.getCompleted());
        item.setDescription(req.getDescription());
        item.setUpdatedAt(LocalDateTime.now());
        item.setDueDate(req.getDueDate());

        log.info("UPDATED ITEM" + item.getDueDate());
        todoItemRepository.save(item);

        return mapper.toResponse(item);
    }

    @Override
    public void deleteToDoItem(long todoId) {
        TodoItem item = todoItemRepository.findById(todoId).orElseThrow(
                () -> new TodoIdNotFoundException("No to do item found"));

        todoItemRepository.delete(item);
    }

    @Override
    @Transactional
    public TodoItemResponse closeTodoItem(long todoId) {
        TodoItem item = todoItemRepository.findById(todoId).orElseThrow(
                () -> new TodoIdNotFoundException("To do item not found"));

        for(SubTask task : item.getSubTask()){
            if(!task.getCompleted()){
                throw new SubTaskNotClosedException("1 or more sub tasked still open and need to complete");
            }
        }

        item.setClosed(true);
        item.setUpdatedAt(LocalDateTime.now());
        log.info("itemId= {} is now closed",todoId);

        return mapper.toResponse(todoItemRepository.save(item));
    }

    @Override
    public TodoItemResponse reopenTodoItem(long todoId) {
        TodoItem item = todoItemRepository.findById(todoId).orElseThrow(
                () -> new TodoIdNotFoundException("To do item not found"));

        item.setClosed(false);
        item.setUpdatedAt(LocalDateTime.now());
        log.info("Reopened itemId= {}", todoId);

        return mapper.toResponse(todoItemRepository.save(item));
    }


}
