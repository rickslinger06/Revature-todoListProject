package com.revature.toDoList.repository;

import com.revature.toDoList.entity.TodoItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    // fetch subtasks in the same query
    @EntityGraph(attributePaths = "subTask")
    Optional<List<TodoItem>> findByUser_Username(String username);

    Optional<List<TodoItem>> findByUser_UserId(String userId);
}
