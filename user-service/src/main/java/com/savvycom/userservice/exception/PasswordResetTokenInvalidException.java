package com.savvycom.userservice.exception;

public class PasswordResetTokenInvalidException extends RuntimeException {
    public PasswordResetTokenInvalidException() {
    }

    public PasswordResetTokenInvalidException(String message) {
        super(message);
    }
}
