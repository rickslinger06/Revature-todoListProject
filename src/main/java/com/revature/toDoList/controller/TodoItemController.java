package com.revature.toDoList.controller;

import com.revature.toDoList.dto.mapper.TodoItemMapper;
import com.revature.toDoList.dto.request.TodoItemCreateRequest;
import com.revature.toDoList.dto.response.TodoItemResponse;
import com.revature.toDoList.services.TodoItemService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class TodoItemController {

    private final TodoItemService todoItemService;

    @PostMapping("/user/add-item")
    public ResponseEntity<TodoItemResponse> createTodoListItem(@Valid @RequestBody TodoItemCreateRequest todoItem){

        TodoItemResponse response = todoItemService.createToDoItem(todoItem);
        log.info("item created successfully {}" ,response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/items/{userId}")
    public ResponseEntity<List<TodoItemResponse>> getAllItemsByUserId(@PathVariable  String userId){

        List<TodoItemResponse> responseItems = todoItemService.getToDoItemByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseItems);


    }

    @GetMapping("/user/item/{todoId}")
    public ResponseEntity<TodoItemResponse> getToDoItemById(@PathVariable  long todoId){

       TodoItemResponse item = todoItemService.getByTodoId(todoId);
        return ResponseEntity.status(HttpStatus.OK).body(item);

    }

    @PutMapping("/user/item/update")
    public ResponseEntity<String> updateToDoItem(@Valid @RequestBody  TodoItemCreateRequest request){
        todoItemService.updateTodoItem(request);
        String message = "Successfully updated item " + request.title();

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping("/user/item/delete/{todoId}")
    public ResponseEntity<String> deleteToDoItem(@PathVariable long todoId){
        todoItemService.deleteToDoItem(todoId);
        String message = "Successfully deleted item " + todoId;

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }



}
