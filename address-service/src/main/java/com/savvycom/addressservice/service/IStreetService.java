package com.savvycom.addressservice.service;

import com.savvycom.addressservice.domain.entity.Street;

import java.util.List;

public interface IStreetService {
    List<Street> findAllProvinceDistrictStreets(Long provinceId, Long districtId);

    Street findById(Long id);
}
