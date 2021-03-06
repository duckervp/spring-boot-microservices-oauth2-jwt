package com.savvycom.addressservice.service.impl;

import com.savvycom.addressservice.domain.entity.Address;
import com.savvycom.addressservice.repository.AddressRepository;
import com.savvycom.addressservice.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address findById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }
}
