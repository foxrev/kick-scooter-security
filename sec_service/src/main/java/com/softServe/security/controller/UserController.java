package com.softServe.security.controller;

import com.softServe.security.converter.UserMapper;
import com.softServe.security.exception.AuthorizationException;
import com.softServe.security.model.AppUser;
import com.softServe.security.model.UserSignInRequest;
import com.softServe.security.model.UserSignUpRequest;
import com.softServe.security.repository.UserRepository;
import com.softServe.security.service.TokenService;
import com.softServe.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

    @PutMapping("/admin/block/{id}")
    public ResponseEntity<Long> blockUser(@PathVariable("id") Long user_id){
        try {
            return ResponseEntity.ok(userService.blockUser(user_id));
        }catch (SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
