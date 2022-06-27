package com.savvycom.userservice.domain.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedMessage<T> extends BaseMessage {
    @Schema(description = "Success request payload")
    private T data;

    public ExtendedMessage(String code, Boolean success, String message, T data) {
        super(code, success, message);
        this.data = data;
    }

    public ExtendedMessage(String code, Boolean success, String message) {
        super(code, success, message);
    }
}
