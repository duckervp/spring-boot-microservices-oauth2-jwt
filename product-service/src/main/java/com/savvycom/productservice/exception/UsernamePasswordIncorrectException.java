package com.savvycom.productservice.exception;

public class UsernamePasswordIncorrectException extends RuntimeException {
    public UsernamePasswordIncorrectException() {
    }

    public UsernamePasswordIncorrectException(String message) {
        super(message);
    }
}
