package com.savvycom.userservice.service.impl;

import com.savvycom.userservice.common.StatusType;
import com.savvycom.userservice.domain.entity.SaleStaff;
import com.savvycom.userservice.repository.SaleStaffRepository;
import com.savvycom.userservice.service.ISaleStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleStaffService implements ISaleStaffService {
    private final SaleStaffRepository saleStaffRepository;

    /**
     * Find all sale staffs
     * @return List of staff
     */
    @Override
    public List<SaleStaff> findAllSaleStaff(Boolean active) {
        List<SaleStaff> saleStaffs = saleStaffRepository.findAll();
        if (Objects.isNull(active)) {
            return saleStaffs;
        } else if(active) {
            return saleStaffs.stream()
                    .filter(SaleStaff::isActive)
                    .collect(Collectors.toList());
        }
        return saleStaffs.stream()
                .filter(saleStaff -> !saleStaff.isActive())
                .collect(Collectors.toList());
    }

    /**
     * Find sale staff info by id
     * @return sale staff entity
     */
    @Override
    public SaleStaff findById(Long id) {
        return saleStaffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found any sale staff with id: " + id));
    }

    /**
     * Create new sale staff
     * @param saleStaff sale staff info
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
