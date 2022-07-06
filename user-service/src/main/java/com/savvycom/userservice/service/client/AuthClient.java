package com.savvycom.userservice.service.client;

import com.savvycom.userservice.config.AuthFeignClientConfig;
import com.savvycom.userservice.domain.feign.AuthInfo;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "http://192.168.196.207:8900", configuration = AuthFeignClientConfig.class)
public interface AuthClient {
    @PostMapping("/oauth/token")
    @Headers("Content-Type: multipart/form-data")
    AuthInfo getToken(@RequestParam("grant_type") String grantType, @RequestParam("username") String username, @RequestParam("password") String password);
}
