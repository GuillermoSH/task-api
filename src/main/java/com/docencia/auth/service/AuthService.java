package com.docencia.auth.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public boolean validateCredentials(String username, String password) {
        return "user".equals(username) && "pass".equals(password);
    }
}