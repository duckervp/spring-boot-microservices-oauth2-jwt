package com.savvycom.userservice.controller;

import com.savvycom.userservice.exception.PasswordResetTokenInvalidException;
import com.savvycom.userservice.exception.UserAlreadyExistException;
import com.savvycom.userservice.exception.UserNotFoundException;
import com.savvycom.userservice.exception.UsernamePasswordIncorrectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends BaseController {

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException() {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", "Request body is required!");
    }

    @ExceptionHandler({ UserAlreadyExistException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(UserAlreadyExistException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {
        if(CollectionUtils.isEmpty(e.getBindingResult().getFieldErrors())) {
            return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                    String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
        }

        String message = e.getBindingResult().getFieldErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), message));
    }


    @ExceptionHandler({ MessagingException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(MessagingException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler({ UnsupportedEncodingException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(UnsupportedEncodingException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler({ UserNotFoundException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(UserNotFoundException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }



    @ExceptionHandler({ PasswordResetTokenInvalidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(PasswordResetTokenInvalidException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }


    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(IllegalArgumentException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler({ UsernamePasswordIncorrectException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(UsernamePasswordIncorrectException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(EntityNotFoundException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleException(AccessDeniedException e) {
        return failedResponse(HttpStatus.FORBIDDEN.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }

    
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception e) {
        return failedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value() + "",
                String.format("%s: %s",e.getClass().getSimpleName(), e.getMessage()));
    }
}
