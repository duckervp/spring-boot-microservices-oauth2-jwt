package com.savvycom.userservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Objects;

@Configuration
public class OAuth2FeignInterceptor implements RequestInterceptor {
    public static final String TOKEN_TYPE = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (Objects.nonNull(authentication) && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, details.getTokenValue()));
        }
    }
}
