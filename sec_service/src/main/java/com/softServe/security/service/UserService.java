package com.softServe.security.service;

import com.softServe.security.exception.AuthorizationException;
import com.softServe.security.model.AppUser;
import com.softServe.security.model.UserSignInRequest;
import com.softServe.security.model.UserSignUpRequest;

public interface UserService {

    AppUser signUp(UserSignUpRequest request) throws AuthorizationException;

    AppUser signIn(UserSignInRequest request) throws AuthorizationException;
}
