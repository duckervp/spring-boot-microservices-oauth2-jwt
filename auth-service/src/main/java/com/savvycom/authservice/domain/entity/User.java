package com.savvycom.authservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
	@NotNull
	private String username;
	@NotNull
	private String password;
	private String firstName;
	private String lastName;
	private String phone;
	private String role;
	private String avatar;
	private Date createdAt;
	private Date modifiedAt;

	public User(String username, String password, String firstName, String lastName, String phone, String role, String avatar) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.role = role;
		this.avatar = avatar;
		this.createdAt = new Date();
	}
}
