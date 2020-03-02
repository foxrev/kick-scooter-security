package com.softserve.example_with_jwt.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.example_with_jwt.model.UserSignInRequest;
import com.softserve.example_with_jwt.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager, TokenService tokenService, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserSignInRequest user = objectMapper.readValue(request.getInputStream(), UserSignInRequest.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),
                    user.getPassword(), null));
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws ServletException {
        String accessToken =
                tokenService.createToken(((User) authResult.getPrincipal()).getUsername(), authResult.getAuthorities());
        response.addHeader("Authorization", accessToken);
    }
}
