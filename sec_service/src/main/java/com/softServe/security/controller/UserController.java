package com.softServe.security.controller;

import com.softServe.security.model.AppUser;
import com.softServe.security.model.UserSignInRequest;
import com.softServe.security.model.UserSignUpRequest;
import com.softServe.security.service.TokenService;
import com.softServe.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody UserSignUpRequest request) {
        try {
            AppUser user = userService.signUp(request);
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", tokenService.createToken(user.getEmail(), user.getRoles()));
            return new ResponseEntity<>(header, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody UserSignInRequest request) {
        try {
            AppUser user = userService.signIn(request);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", tokenService.createToken(user.getEmail(), user.getRoles()));
            return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
