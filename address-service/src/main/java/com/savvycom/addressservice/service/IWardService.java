package com.savvycom.addressservice.service;

import com.savvycom.addressservice.domain.entity.Ward;

import java.util.List;

public interface IWardService {
    List<Ward> findAllProvinceDistrictWards(Long provinceId, Long districtId);

    Ward findById(Long id);
}
