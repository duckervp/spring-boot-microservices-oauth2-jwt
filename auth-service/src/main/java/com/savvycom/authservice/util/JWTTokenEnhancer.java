package com.savvycom.authservice.util;

import com.savvycom.authservice.domain.entity.User;
import com.savvycom.authservice.domain.model.CustomUserDetails;
import com.savvycom.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This class adds user_id field to the original access token
 */

public class JWTTokenEnhancer extends JwtAccessTokenConverter {
    private UserRepository userRepository;

    public JWTTokenEnhancer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Not found any user with username: "+ username));

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user_id", user.getId());
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(userInfo);

        return super.enhance(customAccessToken, authentication);
    }
}
