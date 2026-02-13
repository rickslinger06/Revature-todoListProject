package com.revature.toDoList.repository;

import com.revature.toDoList.entity.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask,Long> {
    List<SubTask> findByTodoItem_todoId(long todoId);
}
