package com.softServe.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softServe.security.model.AppUser;
import com.softServe.security.service.TokenService;
import com.softServe.security.service.impl.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class Security extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    private UserDetailsService userDetailsService;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;

    public Security(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder,
                    TokenService tokenService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**","/callback/", "/webjars/**", "/error**","/sign-in", "/sign-up")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(),tokenService, objectMapper))
                .addFilter(new AuthorizationFilter(authenticationManager(),tokenService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
//todo fix that
    @Bean
    public PrincipalExtractor principalExtractor(){
        return map -> {
            return new AppUser();
        };
    }
}
