package com.savvycom.addressservice.service;

import com.savvycom.addressservice.domain.entity.Address;

import java.util.List;

public interface IAddressService {
    List<Address> findAll();

    Address findById(Long id);
}
