package com.revature.toDoList.controller;

import com.revature.toDoList.dto.RegisterRequest;
import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/make-admin")
    public ResponseEntity<?> makeAdmin(@PathVariable String userId){
       userService.makeUserAdmin(userId);

       return new ResponseEntity(HttpStatus.OK);
    }



}
