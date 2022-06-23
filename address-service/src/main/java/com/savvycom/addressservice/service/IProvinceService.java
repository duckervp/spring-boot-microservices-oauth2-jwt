package com.savvycom.addressservice.service;

import com.savvycom.addressservice.domain.entity.Province;

import java.util.List;

public interface IProvinceService {
    List<Province> findAll();

    Province findById(Long id);
}
