package com.softServe.security.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class KeyConfig {

    @Bean
    public PrivateKey getPrivateKey(@Value("${key.privateKeyPath}") String path) throws Exception {
        Resource resource = new ClassPathResource(path);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));


        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Bean
    public RSAPublicKey getPublicKey(@Value("${key.publicKeyPath}") String path) throws Exception {
        Resource resource = new ClassPathResource(path);
        byte[] keyBytes = Files.readAllBytes(Paths.get(resource.getURI()));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }

}
