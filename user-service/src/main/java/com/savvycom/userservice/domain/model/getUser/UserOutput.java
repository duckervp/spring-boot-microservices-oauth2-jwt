package com.savvycom.userservice.domain.model.getUser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOutput {
    @Schema(description = "User id")
    private Long id;

    @Schema(description = "User email")
    private String username;

    @Schema(description = "User name")
    private String name;

    @Schema(description = "User gender")
    private String gender;

    @Schema(description = "User address")
    private String address;

    @Schema(description = "User phone number")
    private String phone;

    @Schema(description = "User role")
    private String role;

    @Schema(description = "User avatar url")
    private String avatar;

    @Schema(description = "Status of account")
    private Integer active;

    @Schema(description = "Created time")
    private LocalDateTime createdAt;

    @Schema(description = "Updated time")
    private LocalDateTime modifiedAt;
}
