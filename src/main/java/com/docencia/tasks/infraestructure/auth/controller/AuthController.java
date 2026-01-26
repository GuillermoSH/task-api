package com.docencia.tasks.infraestructure.auth.controller;

import com.docencia.tasks.infraestructure.auth.dto.LoginRequest;
import com.docencia.tasks.infraestructure.auth.dto.RegisterRequest;
import com.docencia.tasks.infraestructure.auth.dto.TokenResponse;
import com.docencia.tasks.infraestructure.auth.service.AuthService;
import com.docencia.tasks.infraestructure.auth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login with an existing user")
    public TokenResponse login(@RequestBody LoginRequest req) {
        if (!authService.validateCredentials(req.username(), req.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        try {
            return new TokenResponse(jwtService.generateToken(req.username()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generando token: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }
}