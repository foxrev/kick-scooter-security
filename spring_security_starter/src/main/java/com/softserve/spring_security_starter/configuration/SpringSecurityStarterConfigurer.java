package com.softserve.spring_security_starter.configuration;

import com.softServe.security.security.AuthorizationFilter;
import com.softServe.security.service.TokenService;
import com.softServe.security.service.impl.TokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class SpringSecurityStarterConfigurer {

    private TokenService tokenService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public SpringSecurityStarterConfigurer(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @ConditionalOnMissingBean
    public AuthorizationFilter authorizationFilter(){
        AuthorizationFilter authorizationFilter = new AuthorizationFilter(authenticationManager, tokenService);
        return authorizationFilter;
    }
}
