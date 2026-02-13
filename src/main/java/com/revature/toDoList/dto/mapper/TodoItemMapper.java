package com.revature.toDoList.dto.mapper;

import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.response.TodoItemResponse;
import com.revature.toDoList.entity.TodoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoItemMapper {
    @Mapping(source = "user.userId",target = "userId")
    @Mapping(source = "todoId", target = "todoId")
    TodoItemResponse toResponse(TodoItem todoItem);

    @Mapping(target = "todoId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "completed", ignore = true)
    TodoItem toEntity(TodoItemCreateRequest request);

}
