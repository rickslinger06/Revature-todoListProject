package com.revature.toDoList.exception;

public class UserExistsException extends RuntimeException{
    public UserExistsException(String msg){
        super(msg);
    }
}
