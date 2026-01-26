package com.docencia.tasks.infraestructure.auth.service;

import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secret = "esta_es_una_clave_secreta_muy_larga_y_segura_123456";
    private final long expiration = 30;

    @BeforeEach
    void setUp() {
        // Pasamos los valores manualmente simulando el @Value de Spring
        jwtService = new JwtService(secret, expiration);
    }

    @Test
    void shouldGenerateValidToken() {
        String username = "testUser";
        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void shouldValidateTokenCorrectly() {
        String username = "testUser";
        String token = jwtService.generateToken(username);
        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertTrue(jwtService.isValid(token));
    }

    @Test
    void shouldReturnFalseWhenTokenIsInvalidOrMalformed() {
        String malformedToken = "this.is.not.a.token";

        // Cubre el bloque catch de isValid
        assertFalse(jwtService.isValid(malformedToken));
    }

    @Test
    void shouldFailIfUsernamesDoNotMatch() {
        String token = jwtService.generateToken("userA");
        UserDetails userB = new User("userB", "password", Collections.emptyList());

        assertFalse(jwtService.isTokenValid(token, userB));
    }

    @Test
    void shouldReturnFalseForExpiredToken() {
        // Creamos un service con expiración 0 (o negativa) para forzar expiración inmediata
        JwtService expiredService = new JwtService(secret, -10);
        String token = expiredService.generateToken("userExp");

        assertFalse(jwtService.isValid(token));
    }
}