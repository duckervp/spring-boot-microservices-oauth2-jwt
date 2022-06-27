package com.savvycom.userservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.savvycom.userservice.util.validation.ValidEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPasswordForgotInput {
    @ValidEmail(message = "Username must be a valid email")
    @Schema(description = "User email")
    private String username;
}
