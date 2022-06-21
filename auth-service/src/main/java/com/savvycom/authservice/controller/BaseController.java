package com.savvycom.authservice.controller;

import com.savvycom.authservice.domain.message.BaseMessage;
import com.savvycom.authservice.domain.message.ExtendedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    public <T> ResponseEntity<?> successResponse(String message, String description, T data) {
        ExtendedMessage<T> responseMessage =  new ExtendedMessage<>(
                HttpStatus.OK.value() + "",
                true,
                message,
                description,
                data);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public <T> ResponseEntity<?> successResponse(String message, T data) {
        return successResponse(message, null, data);
    }

    public <T> ResponseEntity<?> successResponse(T data) {
        return successResponse(null, null, data);
    }

    public ResponseEntity<?> successResponse() {
        return successResponse(null, null, null);
    }

    public ResponseEntity<?> failedResponse(String code, String message, String description) {
        BaseMessage responseMessage = new BaseMessage(
                code,
                false,
                message,
                description);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(Integer.parseInt(code)));
    }
}
