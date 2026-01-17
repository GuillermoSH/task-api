package com.docencia.auth.dto;

/**
 * Representa la respuesta que contiene el token JWT.
 */
public record TokenResponse(
        String token
) {}