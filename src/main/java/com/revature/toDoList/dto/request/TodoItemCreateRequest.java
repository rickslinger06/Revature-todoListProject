package com.revature.toDoList.dto.request;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TodoItemCreateRequest(
        Long todoId,
        @NotBlank
        @Size(max =100)
        String title,
        @NotBlank
        String description,
        @FutureOrPresent(message="must be present or future date")
        LocalDate dueDate,
        Boolean completed,
        LocalDateTime updatedAt



) {}
