package com.savvycom.addressservice.repository;

import com.savvycom.addressservice.domain.entity.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
    List<Street> findByProvinceIdAndDistrictId(Long provinceId, Long districtId);
}
