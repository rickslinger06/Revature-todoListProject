package com.revature.toDoList.controller;

import com.revature.toDoList.auth.JwtService;
import com.revature.toDoList.dto.RegisterRequest;
import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.repository.UserRepository;
import com.revature.toDoList.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {

        UserDTO createdUser = userService.registerUser(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

}
