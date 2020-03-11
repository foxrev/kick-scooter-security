package com.softServe.security.controller;

import com.softServe.security.exception.AuthorizationException;
import com.softServe.security.model.AppUser;
import com.softServe.security.model.UserSignInRequest;
import com.softServe.security.model.UserSignUpRequest;
import com.softServe.security.service.TokenService;
import com.softServe.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    //http://localhost:8080/oauth2/authorization/google - google auth URL

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody UserSignUpRequest request) throws AuthorizationException, ServletException {
        AppUser user = userService.signUp(request);
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", tokenService.createToken(user.getEmail(), user.getRoles()));
        return new ResponseEntity<>(header, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody UserSignInRequest request) throws AuthorizationException, ServletException {
        AppUser user = userService.signIn(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.createToken(user.getEmail(), user.getRoles()));
        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/")
    public String welcome(){
        return "Hello World";
    }
}
