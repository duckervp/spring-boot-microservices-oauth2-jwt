package com.savvycom.userservice.controller;

import com.savvycom.userservice.exception.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler extends BaseController {
    @ExceptionHandler({ UserAlreadyExistException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(UserAlreadyExistException e) {
        return failedResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value() + "",
                e.getMessage(),
                "UserAlreadyExistException");
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {
        return failedResponse(
                HttpStatus.BAD_REQUEST.value() + "",
                e.getMessage(),
                "MethodArgumentNotValidException");
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(IllegalArgumentException e) {
        return failedResponse(
                HttpStatus.BAD_REQUEST.value() + "",
                e.getMessage(),
                "IllegalArgumentException");
    }

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception e) {
        return failedResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value() + "",
                e.getMessage(),
                "Exception");
    }
}
