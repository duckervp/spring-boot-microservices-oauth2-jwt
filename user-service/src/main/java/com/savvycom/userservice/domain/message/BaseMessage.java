package com.savvycom.userservice.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseMessage {
    @Schema(description = "Status code of request")
    private String code;

    @Schema(description = "Boolean result if request success or not")
    private Boolean success;

    @Schema(description = "Success message or message describe reason for error")
    private String message;
}
