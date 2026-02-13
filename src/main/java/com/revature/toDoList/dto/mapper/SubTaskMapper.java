package com.revature.toDoList.dto.mapper;

import com.revature.toDoList.dto.request.SubTaskCreateRequest;
import com.revature.toDoList.dto.response.SubTaskResponse;
import com.revature.toDoList.entity.SubTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubTaskMapper {

    @Mapping(target = "subTaskId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "todoItem", ignore = true)
    SubTask toEntity(SubTaskCreateRequest request);
    @Mapping(source="todoItem.todoId",target = "todoId")
    SubTaskResponse toResponse(SubTask subTask);
}
