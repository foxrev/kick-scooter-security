package com.softServe.security.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.softServe.security.configuration.TokenServiceConfigProperties;
import com.softServe.security.model.Roles;
import com.softServe.security.security.AuthenticationImpl;
import com.softServe.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenServiceConfigProperties tokenServiceConfigProperties;
    private final JWSSigner signer;
    private final JWSVerifier verifier;

    @Override
    public String createToken(String email, Collection<? extends GrantedAuthority> roles) throws ServletException {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issuer(tokenServiceConfigProperties.getHost())
                    .claim("role", roles.toArray()[0])
                    .expirationTime(Date.from(Instant.now().plus(tokenServiceConfigProperties.getExpiration(),
                            ChronoUnit.MINUTES)))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);

            signedJWT.sign(signer);

            return "Bearer " + signedJWT.serialize();
        }catch (JOSEException e){
            throw new ServletException(e);
        }
    }

    @Override
    public Date getExpirationTime(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
    }

    @Override
    public JWTClaimsSet getClaims(String token) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        return claimsSet;
    }

    @Override
    public Authentication parse(String token) throws Exception {
        SignedJWT jwt = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        if(jwt.verify(verifier)){
            String email = claimsSet.getSubject();
            List<Roles> roles = new ObjectMapper()
                    .readValue(((String) claimsSet.getClaim("role")), new TypeReference<List<Roles>>() {});
            return AuthenticationImpl.builder()
                    .email(email)
                    .roles(roles)
                    .authenticated(true)
                    .build();
        }
        throw new Exception("Cannot parse token");
    }
}
