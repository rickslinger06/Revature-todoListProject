package com.revature.toDoList.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record SubTaskCreateRequest(

        @NotBlank(message = "Description is required")
        @Max(value = 120, message = "only 120 characters allowed")
        String description,
        Boolean completed

) {}
