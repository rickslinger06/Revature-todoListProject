package com.revature.toDoList.repository;

import com.revature.toDoList.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem,Long> {
    Optional<List<TodoItem>> findByUser_UserId(String userId);
}
