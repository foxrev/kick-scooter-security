package com.softServe.security.converter;

import com.softServe.security.model.AppUser;
import com.softServe.security.model.Roles;
import com.softServe.security.model.UserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public AppUser mapToUser(UserSignUpRequest request){
        AppUser appUser = new AppUser();
        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.USER);
        appUser.setEmail(request.getEmail());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRoles(roles);

        return appUser;
    }
}
