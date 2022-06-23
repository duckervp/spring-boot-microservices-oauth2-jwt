package com.savvycom.addressservice.service.impl;

import com.savvycom.addressservice.domain.entity.Ward;
import com.savvycom.addressservice.repository.WardRepository;
import com.savvycom.addressservice.service.IWardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WardService implements IWardService {
    private final WardRepository wardRepository;

    @Override
    public List<Ward> findAllProvinceDistrictWards(Long provinceId, Long districtId) {
        return wardRepository.findByProvinceIdAndDistrictId(provinceId, districtId);
    }

    @Override
    public Ward findById(Long id) {
        return wardRepository.findById(id).orElse(null);
    }
}
