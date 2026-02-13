package com.revature.toDoList.dto.request;

public record SubTaskCreateRequest(
        String Description,
        Boolean completed
) {}
