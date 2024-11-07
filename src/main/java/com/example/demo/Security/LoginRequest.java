package com.example.demo.Security;

import lombok.Getter;
import lombok.Setter;

/**
 * Format for sending Login Request.*/
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
