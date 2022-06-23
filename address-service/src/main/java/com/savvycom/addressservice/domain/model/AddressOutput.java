package com.savvycom.addressservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressOutput {
    private Long id;
    private Long province;
    private Long district;
    private Long street;
    private Long ward;
    private String specificAddress;
    private String phone;
    private Date createdAt;
    private Date modifiedAt;

}
