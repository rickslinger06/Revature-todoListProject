package com.revature.toDoList.repository;

import com.revature.toDoList.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
