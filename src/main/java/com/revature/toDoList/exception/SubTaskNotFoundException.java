package com.revature.toDoList.exception;

public class SubTaskNotFoundException extends RuntimeException {
    public SubTaskNotFoundException(String subTaskNotFound) {
        super(subTaskNotFound);
    }
}
