package com.softserve.example_with_jwt.service;

import com.softserve.example_with_jwt.exception.AuthorizationException;
import com.softserve.example_with_jwt.model.AppUser;
import com.softserve.example_with_jwt.model.UserSignInRequest;
import com.softserve.example_with_jwt.model.UserSignUpRequest;

public interface UserService {

    AppUser signUp(UserSignUpRequest request) throws AuthorizationException;

    AppUser signIn(UserSignInRequest request) throws AuthorizationException;
}
