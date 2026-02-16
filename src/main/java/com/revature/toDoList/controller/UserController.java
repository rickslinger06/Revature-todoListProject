package com.revature.toDoList.controller;

import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{userId}/make-admin")
    public ResponseEntity<?> makeAdmin(@PathVariable String userId){
       userService.makeUserAdmin(userId);

       return new ResponseEntity(HttpStatus.OK);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId){
        UserDTO dto = userService.getUserById(userId);
        return ResponseEntity.ok(dto);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @PageableDefault(sort = "username", size = 5) Pageable pageable) {

        Page<UserDTO> dtoList = userService.findAllUsers(pageable);
        return ResponseEntity.ok(dtoList);
    }



}
