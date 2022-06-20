package com.savvycom.productservice.controller;

import com.savvycom.productservice.domain.UserOutput;
import com.savvycom.productservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping()

    public List<UserOutput> findAll() {
        return userService.findAll();
    }


    @GetMapping("/test")

    public String test() {
        return "TEST";
    }
}
