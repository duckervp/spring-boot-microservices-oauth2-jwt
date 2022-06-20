package com.savvycom.productservice.service;

import com.savvycom.productservice.domain.UserOutput;

import java.util.List;

public interface UserService {
    List<UserOutput> findAll();
}
