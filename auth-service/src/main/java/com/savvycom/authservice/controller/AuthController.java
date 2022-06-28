package com.savvycom.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

	/**
	 * Verify token from other service
	 *
	 * @param authentication
	 * @return user information
	 */
	@GetMapping("/user")
	public ResponseEntity<?> getUser(OAuth2Authentication authentication) {
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("user", authentication.getPrincipal());
		userInfo.put("authorities", AuthorityUtils
				.authorityListToSet(authentication.getUserAuthentication().getAuthorities()));
		userInfo.put("scope", authentication.getOAuth2Request().getScope());
		return new ResponseEntity<>(userInfo, HttpStatus.OK);
	}
}
