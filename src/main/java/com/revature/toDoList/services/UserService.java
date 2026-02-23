package com.revature.toDoList.services;

import com.revature.toDoList.dto.request.RegisterRequest;
import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.dto.request.ResetPasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO registerUser(RegisterRequest registerRequest);

    UserDTO getUserById(String userId);

    UserDTO getUserByUsername(String username);
    boolean existByUsername(String username);
    void changeUseRole(String userId);

   Page<UserDTO> findAllUsers(Pageable pageable);

    void resetPassword(ResetPasswordRequest passwordRequest);
}
