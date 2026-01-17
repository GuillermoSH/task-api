package com.docencia.auth.dto;

/**
 * Representa los datos necesarios para iniciar sesión.
 */
public record LoginRequest(
        String username,
        String password
) {}