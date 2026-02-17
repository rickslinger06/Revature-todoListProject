package com.revature.toDoList.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoUpdateRequest {

    private long id;
    private String title;
    private String description;
    private Boolean completed;
    private LocalDate dueDate;
}
