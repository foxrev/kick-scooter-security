package com.softServe.security.service;

import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.ServletException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface TokenService {

    String createToken(String email, Collection<? extends GrantedAuthority> roles) throws ServletException;

    Date getExpirationTime(String token) throws ParseException;

    String getSubject(String token) throws ParseException;

    JWTClaimsSet getClaims(String token) throws ParseException;

    Authentication parse(String token) throws Exception;
}
