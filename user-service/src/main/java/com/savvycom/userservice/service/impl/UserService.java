package com.savvycom.userservice.service.impl;

import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.Role;
import com.savvycom.userservice.domain.model.UserOutput;
import com.savvycom.userservice.repository.UserRepository;
import com.savvycom.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<UserOutput> findAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public UserOutput findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return modelMapper.map(user, UserOutput.class);
    }
}
