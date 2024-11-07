package com.example.demo.Security;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * format for login response. Always return JWT token here.*/
@Getter
@Setter
public class LoginResponse {
    private String username;
    private String jwtToken;
    private List<String> roles;

    public LoginResponse(String username, String jwtToken, List<String> roles) {
        this.username = username;
        this.jwtToken = jwtToken;
        this.roles = roles;
    }
}
