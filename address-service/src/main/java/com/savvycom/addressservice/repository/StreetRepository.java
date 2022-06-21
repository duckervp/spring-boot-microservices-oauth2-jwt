package com.savvycom.addressservice.repository;

import com.savvycom.addressservice.domain.entity.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {

}
