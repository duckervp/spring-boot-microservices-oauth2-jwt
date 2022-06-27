package com.savvycom.userservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.savvycom.userservice.util.validation.NotBlankIfNotNull;
import com.savvycom.userservice.util.validation.ValidEmail;
import com.savvycom.userservice.util.validation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInput {
	@ValidEmail(message = "Username must be a valid email")
	@Schema(description = "User email")
	private String username;

	@NotBlank(message = "Name must not be null or empty")
	@Schema(description = "User account password")
	private String password;

	@NotBlank(message = "Name must not be null or empty")
	@Schema(description = "User full name")
	private String name;

	@NotBlankIfNotNull(message = "Gender must not be blank")
	@Schema(description = "User gender")
	private String gender;

	@NotBlankIfNotNull(message = "Address must not be blank")
	@Schema(description = "User address")
	private String address;

	@ValidPhoneNumber(message = "Phone must be a string of 10 or 11 digits")
	@Schema(description = "User phone number")
	private String phone;

	@NotBlankIfNotNull(message = "Avatar url must not be blank")
	@Schema(description = "User avatar url")
	private String avatar;
}
