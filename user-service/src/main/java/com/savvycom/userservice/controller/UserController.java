package com.savvycom.userservice.controller;

import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.Role;
import com.savvycom.userservice.domain.model.UserOutput;
import com.savvycom.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController extends BaseController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<?> register(@RequestBody @Validated @Valid User registerInfo) {
        if (userService.existsByUsername(registerInfo.getUsername())) {
            return failedResponse(
                    HttpStatus.UNPROCESSABLE_ENTITY.value() + "",
                    "Username existed!",
                    "Username already been taken!");
        }

        registerInfo.setPassword(passwordEncoder.encode(registerInfo.getPassword()));
        registerInfo.setRole(Role.USER);

        registerInfo = userService.save(registerInfo);
        if (Objects.nonNull(registerInfo.getId())) {
            return successResponse(
                    "Register successfully!",
                    null);
        }
        return  failedResponse(
                HttpStatus.BAD_REQUEST.value() + "",
                "Register failed!",
                "Unknown error!");
    }
    @GetMapping()
    public ResponseEntity<?> findAll() {
        return successResponse(userService.findAll());
    }
}
