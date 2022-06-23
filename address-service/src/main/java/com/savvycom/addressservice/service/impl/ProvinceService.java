package com.savvycom.addressservice.service.impl;

import com.savvycom.addressservice.domain.entity.Province;
import com.savvycom.addressservice.repository.ProvinceRepository;
import com.savvycom.addressservice.service.IProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService implements IProvinceService {
    private final ProvinceRepository provinceRepository;

    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Province findById(Long id) {
        return provinceRepository.findById(id).orElse(null);
    }
}
