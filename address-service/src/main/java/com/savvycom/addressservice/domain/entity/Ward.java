package com.savvycom.addressservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ward")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ward {
    @Id
    private Long id;
    private String name;
    private String prefix;
    private Long provinceId;
    private Long districtId;
}
