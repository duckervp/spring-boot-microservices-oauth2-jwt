package com.savvycom.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOutput {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String avatar;
    private Date createdAt;
    private Date modifiedAt;
}
