package com.savvycom.productservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
