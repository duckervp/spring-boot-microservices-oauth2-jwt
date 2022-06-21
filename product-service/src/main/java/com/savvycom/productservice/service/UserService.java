package com.savvycom.productservice.service;

import com.savvycom.productservice.domain.model.UserOutput;

import java.util.List;

public interface UserService {
    List<UserOutput> findAll();
}
