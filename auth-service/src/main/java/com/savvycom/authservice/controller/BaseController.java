package com.savvycom.authservice.controller;

import com.savvycom.authservice.domain.message.BaseMessage;
import com.savvycom.authservice.domain.message.ExtendedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    public <T> ResponseEntity<?> successResponse(String message, T data) {
        ExtendedMessage<T> responseMessage =  new ExtendedMessage<>(
                HttpStatus.OK.value() + "",
                true,
                message,
                data);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public <T> ResponseEntity<?> successResponse(String message) {
        return successResponse(message, null);
    }

    public <T> ResponseEntity<?> successResponse(T data) {
        return successResponse(null, data);
    }

    public ResponseEntity<?> failedResponse(String code, String message) {
        BaseMessage responseMessage = new BaseMessage(code, false, message);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(Integer.parseInt(code)));
    }
}
