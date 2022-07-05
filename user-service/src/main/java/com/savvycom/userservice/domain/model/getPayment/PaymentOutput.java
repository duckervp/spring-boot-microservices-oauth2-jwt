package com.savvycom.userservice.domain.model.getPayment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentOutput {
	@Schema(description = "Payment id")
	private Long id;

	@Schema(description = "Payment method name")
	private String paymentType;

	@Schema(description = "Bank provider")
	private String provider;

	@Schema(description = "Account number")
	private String number;

	@Schema(description = "Created time")
	private LocalDateTime createdAt;

	@Schema(description = "Updated time")
	private LocalDateTime modifiedAt;
}
