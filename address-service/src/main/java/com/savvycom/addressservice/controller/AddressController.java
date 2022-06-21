package com.savvycom.addressservice.controller;

import com.savvycom.addressservice.domain.entity.Address;
import com.savvycom.addressservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AddressController extends BaseController {
    private final AddressService addressService;

    @GetMapping()
    public ResponseEntity<?> findAllAddress() {
        return successResponse(addressService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findAddressById(@PathVariable Long id) {
        return successResponse(addressService.findById(id));
    }
}
