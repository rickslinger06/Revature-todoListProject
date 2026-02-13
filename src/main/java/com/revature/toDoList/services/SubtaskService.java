package com.revature.toDoList.services;

import com.revature.toDoList.dto.request.SubTaskCreateRequest;
import com.revature.toDoList.dto.response.SubTaskResponse;
import com.revature.toDoList.entity.SubTask;

import java.util.List;

public interface SubtaskService {

    SubTaskResponse getBySubTaskId(long id);
    List<SubTaskResponse> getAllSubTasksByToDoId(long todoId);
    SubTaskResponse updateSubTask(long subTaskId, SubTaskCreateRequest subTaskCreateRequest);
    void deleteSubTaskById(long id);
    SubTaskResponse createSubTask(long todoId, SubTaskCreateRequest subTaskCreateRequest);



}
