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

    /**
     * When user is authenticated, they only can access their own information
     * This function retrieves authentication info in security context and get user principal,
     * then when user request user info at route user/:userId,
     * it checks for whether the user has userId in the route is the user that authenticated.
     * @param authentication Authentication
     * @param userId User id
     * @return true or false
     */
    public boolean hasUserId(Authentication authentication, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Not found any user with id: " + userId));
        return user.getUsername().equals(authentication.getPrincipal());
    }
}
