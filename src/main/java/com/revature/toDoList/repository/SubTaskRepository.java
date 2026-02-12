package com.revature.toDoList.repository;

import com.revature.toDoList.entity.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTaskRepository extends JpaRepository<SubTask,Long> {
}
