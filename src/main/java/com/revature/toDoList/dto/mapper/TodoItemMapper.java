package com.revature.toDoList.dto.mapper;

import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.response.TodoItemResponse;
import com.revature.toDoList.entity.TodoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { SubTaskMapper.class })
public interface TodoItemMapper {

    @Mapping(source = "user.userId", target = "userId")
    // MapStruct will map List<SubTask> subTask -> List<SubTaskResponse> subTasks
    @Mapping(source = "subTask", target = "subTasks")
    TodoItemResponse toResponse(TodoItem todoItem);

    @Mapping(target = "todoId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "completed", ignore = true)
    @Mapping(target = "subTask", ignore = true) // we don't set subtasks on create from request
    TodoItem toEntity(TodoItemCreateRequest request);
}
