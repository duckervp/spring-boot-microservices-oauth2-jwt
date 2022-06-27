package com.savvycom.userservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${ui.url}")
    private String uiUrl;

    @Value("${passwordResetTokenValidityHours}")
    private int passwordResetTokenValidityHours;
}
