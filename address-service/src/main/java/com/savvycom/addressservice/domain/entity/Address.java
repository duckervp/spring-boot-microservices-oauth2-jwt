package com.savvycom.addressservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long provinceId;
	private Long districtId;
	private Long streetId;
	private Long wardId;
	private String specificAddress;
	private String phone;
	private Date createdAt;
	private Date modifiedAt;
}
