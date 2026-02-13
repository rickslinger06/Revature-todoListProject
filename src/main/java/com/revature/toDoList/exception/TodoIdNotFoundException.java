package com.revature.toDoList.exception;

public class TodoIdNotFoundException extends RuntimeException{
    public TodoIdNotFoundException(String toDoIdNotFound) {
        super(toDoIdNotFound);
    }
}
