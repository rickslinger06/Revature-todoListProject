package com.revature.toDoList.dto.mapper;

import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.response.TodoItemResponse;
import com.revature.toDoList.entity.TodoItem;
import com.revature.toDoList.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-13T12:58:41-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Microsoft)"
)
@Component
public class TodoItemMapperImpl implements TodoItemMapper {

    @Override
    public TodoItemResponse toResponse(TodoItem todoItem) {
        if ( todoItem == null ) {
            return null;
        }

        String userId = null;
        long todoId = 0L;
        String title = null;
        String description = null;
        LocalDate dueDate = null;
        boolean completed = false;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        userId = todoItemUserUserId( todoItem );
        todoId = todoItem.getTodoId();
        title = todoItem.getTitle();
        description = todoItem.getDescription();
        dueDate = todoItem.getDueDate();
        completed = todoItem.isCompleted();
        createdAt = todoItem.getCreatedAt();
        updatedAt = todoItem.getUpdatedAt();

        TodoItemResponse todoItemResponse = new TodoItemResponse( todoId, title, description, dueDate, completed, createdAt, updatedAt, userId );

        return todoItemResponse;
    }

    @Override
    public TodoItem toEntity(TodoItemCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        TodoItem todoItem = new TodoItem();

        todoItem.setTitle( request.title() );
        todoItem.setDescription( request.description() );
        todoItem.setDueDate( request.dueDate() );
        todoItem.setUpdatedAt( request.updatedAt() );

        return todoItem;
    }

    private String todoItemUserUserId(TodoItem todoItem) {
        if ( todoItem == null ) {
            return null;
        }
        User user = todoItem.getUser();
        if ( user == null ) {
            return null;
        }
        String userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
