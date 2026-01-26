package com.docencia.tasks.infraestructure.auth.dto;

/**
 * Representa los datos necesarios para iniciar sesión.
 */
public record LoginRequest(
        String username,
        String password
) {}