package com.savvycom.productservice.client;

import com.savvycom.productservice.domain.UserOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/users")
    List<UserOutput> findAllUsers();
}
