package com.revature.toDoList.dto.response;

import java.time.LocalDateTime;

public record SubTaskResponse(
        long subTaskId,
        String description,
        Boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long todoId
) {
}
