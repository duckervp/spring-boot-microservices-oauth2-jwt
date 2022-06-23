package com.savvycom.addressservice.repository;

import com.savvycom.addressservice.domain.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByProvinceIdAndDistrictId(Long provinceId, Long districtId);
}
