package com.docencia.tasks.infraestructure.auth.dto;

/**
 * Representa la respuesta que contiene el token JWT.
 */
public record TokenResponse(
        String token
) {}