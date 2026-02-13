package com.revature.toDoList.dto.response;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TodoItemResponse(

        String title,

        String description,

        LocalDate dueDate,
        boolean completed,
        LocalDateTime createdAt,
       LocalDateTime updatedAt,
        String userId

) { }
