package com.revature.toDoList.controller;

import com.revature.toDoList.dto.request.SubTaskCreateRequest;
import com.revature.toDoList.dto.response.SubTaskResponse;
import com.revature.toDoList.services.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubTaskController {

    private final SubtaskService subtaskService;

    @PostMapping("/user/task/add/{todoId}")
    public ResponseEntity<SubTaskResponse> createSubTask(@PathVariable long todoId,
                                                         @Valid @RequestBody SubTaskCreateRequest subRequest) {
        SubTaskResponse subResponse = subtaskService.createSubTask(todoId, subRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(subResponse);
    }

    @PutMapping("/user/task/update/{subId}")
    public ResponseEntity<SubTaskResponse> updateSubTask(@PathVariable long subId,
                                                         @Valid @RequestBody SubTaskCreateRequest subTaskCreateRequest) {
        SubTaskResponse subTaskResponse = subtaskService.updateSubTask(subId, subTaskCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(subTaskResponse);

    }

    @GetMapping("/user/task/{subId}")
    public ResponseEntity<SubTaskResponse> getSubTaskById(@PathVariable long id) {

        SubTaskResponse task = subtaskService.getBySubTaskId(id);

        return ResponseEntity.status(HttpStatus.OK).body(task);

    }

    @GetMapping("/user/task/{todoId}/items")
    public ResponseEntity<List<SubTaskResponse>> getAllSubTaskByTodoItemId(@PathVariable long todoId) {

        List<SubTaskResponse> responseList = subtaskService.getAllSubTasksByToDoId(todoId);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @DeleteMapping("/user/task/delete/{subId}")
    public ResponseEntity<String> deleteSubTask(@PathVariable long subId){

        subtaskService.deleteSubTaskById(subId);

        String message = "Subtask successfully deleted";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
