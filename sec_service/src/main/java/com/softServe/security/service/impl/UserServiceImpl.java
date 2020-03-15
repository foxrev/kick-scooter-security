package com.softServe.security.service.impl;

import com.softServe.security.exception.AuthorizationException;
import com.softServe.security.converter.UserMapper;
import com.softServe.security.model.AppUser;
import com.softServe.security.model.UserSignInRequest;
import com.softServe.security.model.UserSignUpRequest;
import com.softServe.security.repository.UserRepository;
import com.softServe.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public AppUser signUp(UserSignUpRequest request) throws AuthorizationException {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AuthorizationException("User with a suchlike email already exist");
        }else{
            return userRepository.save(userMapper.mapToUser(request));
        }
    }

    @Override
    @Transactional
    public AppUser signIn(UserSignInRequest request) throws AuthorizationException {
        AppUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthorizationException("Email wrong or doesn't exist"));
        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return user;
        }
        throw new AuthorizationException("Incorrect password");
    }

    @Override
    @Transactional
    public long blockUser(String email){
        int resultOfUpdating = userRepository.updateBlockedStatus(email);
        if(resultOfUpdating == 0){
            throw new UsernameNotFoundException("User with the id " + email + " doesn't exist");
        }else{
            return resultOfUpdating;
        }
    }
}
