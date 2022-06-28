package com.savvycom.productservice.controller;

import com.savvycom.productservice.exception.PasswordResetTokenInvalidException;
import com.savvycom.productservice.exception.UserAlreadyExistException;
import com.savvycom.productservice.exception.UserNotFoundException;
import com.savvycom.productservice.exception.UsernamePasswordIncorrectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {
        if(CollectionUtils.isEmpty(e.getBindingResult().getFieldErrors())) {
            return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
        }

        String message = e.getBindingResult().getFieldErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", message);
    }


    @ExceptionHandler({ MessagingException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(MessagingException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
    }

    @ExceptionHandler({ UnsupportedEncodingException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(UnsupportedEncodingException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
    }

    @ExceptionHandler({ UserNotFoundException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(UserNotFoundException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
    }



    @ExceptionHandler({ PasswordResetTokenInvalidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(PasswordResetTokenInvalidException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
    }


    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(IllegalArgumentException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
    }

    @ExceptionHandler({ UsernamePasswordIncorrectException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(UsernamePasswordIncorrectException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(EntityNotFoundException e) {
        return failedResponse(HttpStatus.BAD_REQUEST.value() + "", e.getMessage());
    }

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception e) {
        return failedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value() + "", e.getMessage());
    }
}
