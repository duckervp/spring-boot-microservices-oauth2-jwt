package com.savvycom.addressservice.controller;

import com.savvycom.addressservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController extends BaseController {
    private final IAddressService addressService;

    private final IProvinceService provinceService;

    private final IDistrictService districtService;

    private final IWardService wardService;

    private final IStreetService streetService;

    @GetMapping()
    public ResponseEntity<?> findAllAddress() {
        return successResponse(addressService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAddressById(@PathVariable Long id) {
        return successResponse(addressService.findById(id));
    }

    @GetMapping("/province")
    public ResponseEntity<?> findAllProvinces() {
        return successResponse(provinceService.findAll());
    }

    @GetMapping("/district")
    public ResponseEntity<?> findAllProvinceDistricts(@RequestParam Long provinceId) {
        return successResponse(districtService.findAllProvinceDistricts(provinceId));
    }

    @GetMapping("/ward")
    public ResponseEntity<?> findAllProvinceDistrictWards(
            @RequestParam Long provinceId,
            @RequestParam Long districtId) {
        return successResponse(wardService.findAllProvinceDistrictWards(provinceId, districtId));
    }

    @GetMapping("/street")
    public ResponseEntity<?> findAllProvinceDistrictStreets(
            @RequestParam Long provinceId,
            @RequestParam Long districtId) {
        return successResponse(streetService.findAllProvinceDistrictStreets(provinceId, districtId));
    }


}
