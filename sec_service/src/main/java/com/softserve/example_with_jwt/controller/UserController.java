package com.softserve.example_with_jwt.controller;

import com.softserve.example_with_jwt.model.AppUser;
import com.softserve.example_with_jwt.model.UserSignInRequest;
import com.softserve.example_with_jwt.model.UserSignUpRequest;
import com.softserve.example_with_jwt.service.TokenService;
import com.softserve.example_with_jwt.service.UserService;
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
    public ResponseEntity signUp(@RequestBody UserSignUpRequest request) {
        try {
            AppUser user = userService.signUp(request);
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", tokenService.createToken(user.getEmail(), user.getRoles()));
            return new ResponseEntity<>(header, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@RequestBody UserSignInRequest request){
        try {
            AppUser user = userService.signIn(request);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", tokenService.createToken(user.getEmail(), user.getRoles()));
            return new ResponseEntity(headers, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/greeting")
    public ResponseEntity<String> greeting(){
        return ResponseEntity.ok("Hello World");
    }
}
