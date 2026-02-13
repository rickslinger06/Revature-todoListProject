package com.revature.toDoList.exception;

public class ToDoListItemNotFoundException extends RuntimeException {
    public ToDoListItemNotFoundException(String noToDoListFound) {
        super(noToDoListFound);

    }
}
