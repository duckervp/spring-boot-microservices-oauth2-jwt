package com.savvycom.userservice.util.security;

import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.exception.UserNotFoundException;
import com.savvycom.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {
    private final UserRepository userRepository;

    public boolean hasUserId(Authentication authentication, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Not found any user with id: " + userId));
        return user.getUsername().equals(authentication.getPrincipal());
    }
}
