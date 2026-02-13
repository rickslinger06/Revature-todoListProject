package com.revature.toDoList.dto.request;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record SubTaskCreateRequest(

        @NotBlank(message = "Description is required")
        String description,
        Boolean completed

) {}
