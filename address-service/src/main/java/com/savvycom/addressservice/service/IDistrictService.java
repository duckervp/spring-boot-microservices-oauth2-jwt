package com.savvycom.addressservice.service;

import com.savvycom.addressservice.domain.entity.District;

import java.util.List;

public interface IDistrictService {
    List<District> findAllProvinceDistricts(Long provinceId);

    District findById(Long id);
}
