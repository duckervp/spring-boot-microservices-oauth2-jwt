package com.savvycom.authservice.domain.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedMessage<T> extends BaseMessage {
    private T data;

    public ExtendedMessage(String code, Boolean success, String message, T data) {
        super(code, success, message);
        this.data = data;
    }
}
