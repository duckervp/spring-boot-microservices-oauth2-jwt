package com.savvycom.userservice.service;

import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.UserOutput;

import java.util.List;

public interface UserService {
    List<UserOutput> findAll();

    boolean existsByUsername(String username);

    User save(User user);
}
