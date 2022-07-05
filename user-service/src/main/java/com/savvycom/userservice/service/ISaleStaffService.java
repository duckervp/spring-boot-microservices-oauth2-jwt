package com.savvycom.userservice.service;

import com.savvycom.userservice.domain.entity.SaleStaff;

import java.util.List;

public interface ISaleStaffService {
    List<String> findAllSaleStaffName();

    void create(SaleStaff saleStaff);

    void delete(Long id);
}
