package com.savvycom.addressservice.service.impl;

import com.savvycom.addressservice.domain.entity.Street;
import com.savvycom.addressservice.repository.StreetRepository;
import com.savvycom.addressservice.service.IStreetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreetService implements IStreetService {
    private final StreetRepository streetRepository;

    @Override
    public List<Street> findAllProvinceDistrictStreets(Long provinceId, Long districtId) {
        return streetRepository.findByProvinceIdAndDistrictId(provinceId, districtId);
    }

    @Override
    public Street findById(Long id) {
        return streetRepository.findById(id).orElse(null);
    }
}
