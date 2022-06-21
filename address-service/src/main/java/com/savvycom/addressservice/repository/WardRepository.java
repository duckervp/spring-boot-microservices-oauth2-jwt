package com.savvycom.addressservice.repository;

import com.savvycom.addressservice.domain.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {

}
