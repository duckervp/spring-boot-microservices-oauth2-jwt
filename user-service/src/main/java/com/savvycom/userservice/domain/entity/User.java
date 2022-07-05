package com.savvycom.userservice.domain.entity;

import com.savvycom.userservice.util.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String name;
	private String gender;
	private String address;
	private String phone;
	private String role;
	private String avatar;
	private boolean active;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String passwordResetToken;
	private LocalDateTime passwordResetTokenExpiryDate;
}
