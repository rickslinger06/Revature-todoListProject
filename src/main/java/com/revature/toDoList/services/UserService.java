package com.revature.toDoList.services;

import com.revature.toDoList.dto.request.RegisterRequest;
import com.revature.toDoList.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO registerUser(RegisterRequest registerRequest);

    UserDTO getUserById(String userId);

    UserDTO getUserByUsername(String username);
    boolean existByUsername(String username);
    void makeUserAdmin(String userId);

    List<UserDTO> findAllUsers();
}
