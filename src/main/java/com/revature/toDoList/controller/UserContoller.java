package com.revature.toDoList.controller;

import com.revature.toDoList.dto.RegisterRequest;
import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserContoller {

    private final UserService userService;



}
