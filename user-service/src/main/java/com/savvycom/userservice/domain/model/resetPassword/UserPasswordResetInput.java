package com.savvycom.userservice.domain.model.resetPassword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPasswordResetInput {
    @NotBlank(message = "Password reset token must not be null or empty")
    @Schema(description = "Password reset token")
    private String passwordResetToken;

    @NotBlank(message = "New password must not be null or empty")
    @Schema(description = "User account new password")
    private String newPassword;
}
