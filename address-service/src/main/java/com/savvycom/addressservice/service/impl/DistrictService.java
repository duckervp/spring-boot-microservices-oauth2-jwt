package com.savvycom.addressservice.service.impl;

import com.savvycom.addressservice.domain.entity.District;
import com.savvycom.addressservice.repository.DistrictRepository;
import com.savvycom.addressservice.service.IDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictService implements IDistrictService {
    private final DistrictRepository districtRepository;

    @Override
    public List<District> findAllProvinceDistricts(Long provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }

    @Override
    public District findById(Long id) {
        return districtRepository.findById(id).orElse(null);
    }
}
