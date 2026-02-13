package com.revature.toDoList.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private LocalDateTime localDateTime;
    private String message;
    private  String path;
}
