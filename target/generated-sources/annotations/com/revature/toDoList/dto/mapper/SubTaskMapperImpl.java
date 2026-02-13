package com.revature.toDoList.dto.mapper;

import com.revature.toDoList.dto.request.SubTaskCreateRequest;
import com.revature.toDoList.dto.response.SubTaskResponse;
import com.revature.toDoList.entity.SubTask;
import com.revature.toDoList.entity.TodoItem;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-13T11:28:56-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Microsoft)"
)
@Component
public class SubTaskMapperImpl implements SubTaskMapper {

    @Override
    public SubTask toEntity(SubTaskCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        SubTask subTask = new SubTask();

        subTask.setDescription( request.description() );
        subTask.setCompleted( request.completed() );

        return subTask;
    }

    @Override
    public SubTaskResponse toResponse(SubTask subTask) {
        if ( subTask == null ) {
            return null;
        }

        long todoId = 0L;
        long subTaskId = 0L;
        String description = null;
        Boolean completed = null;
        LocalDateTime createdAt = null;

        todoId = subTaskTodoItemTodoId( subTask );
        subTaskId = subTask.getSubTaskId();
        description = subTask.getDescription();
        completed = subTask.getCompleted();
        createdAt = subTask.getCreatedAt();

        SubTaskResponse subTaskResponse = new SubTaskResponse( subTaskId, description, completed, createdAt, todoId );

        return subTaskResponse;
    }

    private long subTaskTodoItemTodoId(SubTask subTask) {
        if ( subTask == null ) {
            return 0L;
        }
        TodoItem todoItem = subTask.getTodoItem();
        if ( todoItem == null ) {
            return 0L;
        }
        long todoId = todoItem.getTodoId();
        return todoId;
    }
}
