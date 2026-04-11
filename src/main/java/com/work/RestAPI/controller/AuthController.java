package com.work.RestAPI.controller;

import com.work.RestAPI.dto.AuthRequest;
import com.work.RestAPI.service.AuthService;
import com.work.RestAPI.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public AuthController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {

        if (!authService.authenticate(request.getUsername(), request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(request.getUsername());
    }
}
