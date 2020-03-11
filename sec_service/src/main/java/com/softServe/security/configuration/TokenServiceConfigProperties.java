package com.softServe.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("token")
public class TokenServiceConfigProperties {
    private String host;
    private Long expiration;
}
