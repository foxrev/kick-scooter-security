package com.softServe.security.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softServe.security.model.Roles;
import com.softServe.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private TokenService tokenService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (Date.from(Instant.now()).compareTo(tokenService.getExpirationTime(token.substring(7))) > 0) {
                token = tokenService.createToken(tokenService.getClaims(token.substring(7)).getSubject(),
                        new ObjectMapper().readValue(((String) tokenService.getClaims(token.substring(7)).getClaim("role")), new TypeReference<List<Roles>>() {}));
            }
        }catch (ParseException e){
            chain.doFilter(request, response);
        }

        try {
            SecurityContextHolder.getContext().setAuthentication((tokenService.parse(token.substring(7))));
        } catch (Exception e) {
            logger.debug("In doFilterInternal(): ", e);
        } finally {
            chain.doFilter(request, response);
        }
    }
}
