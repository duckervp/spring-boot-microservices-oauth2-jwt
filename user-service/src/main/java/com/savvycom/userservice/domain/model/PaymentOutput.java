package com.savvycom.userservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOutput {
	private Long id;
	private String paymentType;
	private String provider;
	private String number;
	private Date createdAt;
	private Date modifiedAt;
}
