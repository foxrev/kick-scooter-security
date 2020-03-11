package com.softServe.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("key")
public class KeyConfigurationProperties {
    private Resource privateKeyPath;
    private Resource publicKeyPath;
}
