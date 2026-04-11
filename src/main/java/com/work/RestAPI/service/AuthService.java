package com.work.RestAPI.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public boolean authenticate(String username, String password) {
        return "admin".equals(username) && "password".equals(password);
    }
}
