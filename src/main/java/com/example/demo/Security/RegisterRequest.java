package com.example.demo.Security;

import com.example.demo.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


/**
 * Class format to accept registration request in.
 * Contains user properties required for user creation.*/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class    RegisterRequest {
    private String username;
    private String password;
    private String email;
    private Set<Role> roles;
}
