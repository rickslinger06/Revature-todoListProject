package com.revature.toDoList.exception;

public class SubTaskNotClosedException extends RuntimeException {
    public SubTaskNotClosedException(String s) {
        super(s);
    }
}
