package com.softserve.example_with_jwt.mapper;

import com.softserve.example_with_jwt.model.AppUser;
import com.softserve.example_with_jwt.model.Roles;
import com.softserve.example_with_jwt.model.UserSignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapper {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser mapToUser(UserSignUpRequest request){
        AppUser appUser = new AppUser();
        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.ROLE_USER);
        appUser.setEmail(request.getEmail());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRoles(roles);

        return appUser;
    }
}
