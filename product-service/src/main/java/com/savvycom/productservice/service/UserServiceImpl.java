package com.savvycom.productservice.service;

import com.savvycom.productservice.domain.entity.User;
import com.savvycom.productservice.domain.model.UserOutput;
import com.savvycom.productservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<UserOutput> findAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserOutput.class))
                .collect(Collectors.toList());
    }

}
