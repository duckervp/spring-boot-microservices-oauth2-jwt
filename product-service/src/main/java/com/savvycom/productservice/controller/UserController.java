package com.savvycom.productservice.controller;

import com.savvycom.productservice.client.UserServiceClient;
import com.savvycom.productservice.domain.UserOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class UserController {
    private final UserServiceClient userServiceClient;

    @GetMapping()
    @PreAuthorize("hasAuthority('admin')")
    public List<UserOutput> findAll() {
        return userServiceClient.findAllUsers();
    }


    @GetMapping("/test")

    public String test() {
        return "TEST";
    }
}
