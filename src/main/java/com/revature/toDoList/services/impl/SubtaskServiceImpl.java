package com.revature.toDoList.services.impl;

import com.revature.toDoList.dto.mapper.SubTaskMapper;
import com.revature.toDoList.dto.request.SubTaskCreateRequest;
import com.revature.toDoList.dto.response.SubTaskResponse;
import com.revature.toDoList.entity.SubTask;
import com.revature.toDoList.entity.TodoItem;
import com.revature.toDoList.exception.SubTaskNotFoundException;
import com.revature.toDoList.exception.TodoIdNotFoundException;
import com.revature.toDoList.repository.SubTaskRepository;
import com.revature.toDoList.repository.TodoItemRepository;
import com.revature.toDoList.repository.UserRepository;
import com.revature.toDoList.services.SubtaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubtaskServiceImpl implements SubtaskService {


    private final SubTaskRepository subTaskRepository;
    private final SubTaskMapper mapper;
    private final TodoItemRepository todoItemRepository;
    private UserRepository userRepository;

    @Override
    public SubTaskResponse createSubTask(long todoId, SubTaskCreateRequest req) {

        TodoItem item = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new TodoIdNotFoundException("Todo item not found"));

        SubTask subTask = new SubTask();
        subTask.setDescription(req.description());

        boolean completed = Boolean.TRUE.equals(req.completed());
        subTask.setCompleted(completed);

        subTask.setTodoItem(item);

        SubTask savedTask = subTaskRepository.save(subTask);

        log.info("Subtask saved: subTaskId={}, todoId={}", savedTask.getSubTaskId(), todoId);

        return mapper.toResponse(savedTask);
    }


    @Override
    public SubTaskResponse getBySubTaskId(long id) {

        SubTask task = subTaskRepository.findById(id).orElseThrow(
                () -> new SubTaskNotFoundException("SubTask not found"));
        return mapper.toResponse(task);
    }

    @Override
    public List<SubTaskResponse> getAllSubTasksByToDoId(long todoId) {

        TodoItem item = todoItemRepository.findById(todoId).orElseThrow(
                () -> new TodoIdNotFoundException("To do item not found"));
        List<SubTask> listOfTask = subTaskRepository.findByTodoItem_todoId(todoId);

        return listOfTask.stream().map(mapper::toResponse).toList();
    }

    @Override
    public SubTaskResponse updateSubTask(long subTaskId, SubTaskCreateRequest subTaskCreateRequest) {

        SubTask updateTask = subTaskRepository.findById(subTaskId).orElseThrow(() -> new SubTaskNotFoundException(
                "SubTask does not exist"
        ));

        updateTask.setCompleted(subTaskCreateRequest.completed());
        updateTask.setDescription(subTaskCreateRequest.description());
        updateTask.setUpdatedAt(LocalDateTime.now());

        subTaskRepository.save(updateTask);
        log.info("updated Subtask Successfully");
        return mapper.toResponse(updateTask);
    }

    @Override
    public void deleteSubTaskById(long id) {

        SubTask task = subTaskRepository.findById(id).orElseThrow(() -> new SubTaskNotFoundException(
                "SubTask not found"
        ));
        subTaskRepository.delete(task);
        log.info("Subtask name: {} was deleted successfully,",task.getDescription());
    }


}
