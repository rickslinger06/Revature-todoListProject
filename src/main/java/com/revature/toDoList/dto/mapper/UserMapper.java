package com.revature.toDoList.dto.mapper;

import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.entity.User;

public class UserMapper {

    public static User toEntity(UserDTO dto){
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        user.setEmail(dto.getEmail());
        return user;

    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

//        UserDTO dto = UserDTO.builder()
//                .userId(user.getUserId())
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .role(user.getRole())
//                .build();

        return dto;
    }
}
