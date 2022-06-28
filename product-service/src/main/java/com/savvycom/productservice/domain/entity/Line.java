package com.savvycom.productservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "line")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String desc;

    private Long categoryId;

    private Long discountId;

    private Boolean active;

    private Date createdAt;

    private Date modifiedAt;
}
