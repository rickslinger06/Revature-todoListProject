package com.revature.toDoList.exception;

import com.revature.toDoList.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundFoundException ex, HttpServletRequest req){

        ErrorResponse error = new ErrorResponse();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMessage(ex.getMessage());
        error.setPath(req.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserExistsException ex, HttpServletRequest req){

        ErrorResponse error = new ErrorResponse();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMessage(ex.getMessage());
        error.setPath(req.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUserAuth(UserAuthenticationException ex, HttpServletRequest req){

        ErrorResponse error = new ErrorResponse();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMessage(ex.getMessage());
        error.setPath(req.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(
            InvalidTokenException ex,
            HttpServletRequest req) {

        ErrorResponse error = new ErrorResponse();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMessage(ex.getMessage());
        error.setPath(req.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest req) {

        ErrorResponse error = new ErrorResponse();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMessage(ex.getMessage());
        error.setPath(req.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
