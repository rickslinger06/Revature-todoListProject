package com.revature.toDoList.exception;

public class UserAuthenticationException extends RuntimeException {
    public UserAuthenticationException(String noAuthenticatedUserFound) {
        super(noAuthenticatedUserFound);
    }
}
