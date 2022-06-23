package com.savvycom.userservice.service;

import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.UserOutput;

import java.util.List;

public interface IUserService {
    List<UserOutput> findAll();

    boolean existsByUsername(String username);

    User register(User user);

    UserOutput findById(Long id);
}
