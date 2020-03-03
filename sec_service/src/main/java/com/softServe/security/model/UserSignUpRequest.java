package com.softServe.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequest {

    private String email;

    private String firstName;

    private String lastName;

    private String password;
    
}
