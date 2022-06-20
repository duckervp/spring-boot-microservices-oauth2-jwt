package com.savvycom.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public 	Map<String, Object> getUser(OAuth2Authentication authentication) {
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put(
				"user",
				authentication.getPrincipal());
		userInfo.put(
				"authorities",
				AuthorityUtils.authorityListToSet(
						authentication.getUserAuthentication()
								.getAuthorities()));
		userInfo.put("scope",
				authentication.getOAuth2Request().getScope());
		return userInfo;
	}
}
