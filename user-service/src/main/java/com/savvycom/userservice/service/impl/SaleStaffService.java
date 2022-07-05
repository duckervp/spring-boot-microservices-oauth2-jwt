package com.savvycom.userservice.service.impl;

import com.savvycom.userservice.common.StatusType;
import com.savvycom.userservice.domain.entity.SaleStaff;
import com.savvycom.userservice.repository.SaleStaffRepository;
import com.savvycom.userservice.service.ISaleStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleStaffService implements ISaleStaffService {
    private final SaleStaffRepository saleStaffRepository;

    /**
     * Find all sale staff names
     * @return List of staff name
     */
    @Override
    public List<String> findAllSaleStaffName() {
        return saleStaffRepository.findAll().stream()
                .filter(SaleStaff::isActive)
                .map(saleStaff -> saleStaff.getName())
                .collect(Collectors.toList());
    }

    /**
     * Create new sale staff
     * @param saleStaff sale staff name
     */
    @Override
    public void create(SaleStaff saleStaff) {
        saleStaff.setId(null);
        saleStaffRepository.save(saleStaff);
    }

    /**
     * Deactivate sale staff
     * @param id sale staff id
     */
    @Override
    public void delete(Long id) {
        SaleStaff saleStaff = saleStaffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found any sale staff with id: " + id));
        saleStaff.setActive(StatusType.IN_ACTIVE);
        saleStaffRepository.save(saleStaff);
    }
}
