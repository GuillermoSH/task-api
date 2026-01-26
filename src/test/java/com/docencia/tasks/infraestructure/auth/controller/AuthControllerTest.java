package com.docencia.tasks.infraestructure.auth.controller;

import com.docencia.tasks.infraestructure.auth.dto.LoginRequest;
import com.docencia.tasks.infraestructure.auth.dto.RegisterRequest;
import com.docencia.tasks.infraestructure.auth.dto.TokenResponse;
import com.docencia.tasks.infraestructure.auth.service.AuthService;
import com.docencia.tasks.infraestructure.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        // Instanciación manual para evitar problemas de ApplicationContext
        authController = new AuthController(authService, jwtService);
    }

    // --- Tests para Login ---

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        LoginRequest req = new LoginRequest("user", "pass");
        String mockToken = "generated.jwt.token";

        when(authService.validateCredentials("user", "pass")).thenReturn(true);
        when(jwtService.generateToken("user")).thenReturn(mockToken);

        TokenResponse response = authController.login(req);

        assertNotNull(response);
        assertEquals(mockToken, response.token());
        verify(authService).validateCredentials("user", "pass");
        verify(jwtService).generateToken("user");
    }

    @Test
    void login_ShouldThrowUnauthorized_WhenCredentialsAreInvalid() {
        LoginRequest req = new LoginRequest("wrong", "pass");
        when(authService.validateCredentials("wrong", "pass")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                authController.login(req)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Invalid credentials", exception.getReason());
    }

    @Test
    void login_ShouldThrowInternalError_WhenTokenGenerationFails() {
        // Este test cubre el bloque try-catch y la rama del 500
        LoginRequest req = new LoginRequest("user", "pass");
        when(authService.validateCredentials("user", "pass")).thenReturn(true);
        when(jwtService.generateToken("user")).thenThrow(new RuntimeException("System error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                authController.login(req)
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Error generando token"));
    }

    // --- Tests para Register ---

    @Test
    void register_ShouldReturnOk_WhenRegistrationSucceeds() {
        RegisterRequest req = new RegisterRequest("newuser", "pass", "ADMIN");

        // No necesitamos stubbing de authService.register porque es void y no lanza excepción aquí
        ResponseEntity<String> response = authController.register(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(authService).register(req);
    }
}