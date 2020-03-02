package com.softserve.example_with_jwt.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.softserve.example_with_jwt.model.Roles;
import com.softserve.example_with_jwt.security.AuthenticationImpl;
import com.softserve.example_with_jwt.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    //Add vault
    @Value("${host.name}")
    private String host;

    @Value("${token.expiration}")
    private Long expiration;

    private final JWSSigner signer;

    private final JWSVerifier verifier;

    private PrivateKey privateKey;
    private RSAPublicKey publicKey;

    // TODO: 27.02.20 Add ip claim

    @Autowired
    public TokenServiceImpl(PrivateKey privateKey, RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        signer = new RSASSASigner(privateKey);
        verifier = new RSASSAVerifier(publicKey);
    }

    @Override
    public String createToken(String email, Collection<? extends GrantedAuthority> roles) throws ServletException {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issuer(host)
                    .claim("role", new ObjectMapper().writeValueAsString(roles))
                    .expirationTime(Date.from(Instant.now().plus(expiration, ChronoUnit.MINUTES)))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);

            signedJWT.sign(signer);

            return "Bearer " + signedJWT.serialize();
        }catch (JOSEException | JsonProcessingException e){
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
