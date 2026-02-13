package com.revature.toDoList.services;

import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.response.TodoItemResponse;

import java.util.List;

public interface TodoItemService {

    TodoItemResponse createToDoItem(TodoItemCreateRequest todoItem);
    List<TodoItemResponse> getToDoItemByUserId(String userId);
    TodoItemResponse getByTodoId(long todoId);

    TodoItemResponse updateTodoItem(TodoItemCreateRequest req);
    void deleteToDoItem(long todoId);

}
