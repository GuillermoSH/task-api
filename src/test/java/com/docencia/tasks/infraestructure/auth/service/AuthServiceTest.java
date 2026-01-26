package com.docencia.tasks.infraestructure.auth.service;

import com.docencia.tasks.adapters.out.persistence.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.UserRepository;
import com.docencia.tasks.infraestructure.auth.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    // --- Tests para validateCredentials ---

    @Test
    void validateCredentials_ShouldReturnTrue_WhenUserExistsAndPasswordsMatch() {
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("user1");
        user.setPassword("hashedPass");

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPass", "hashedPass")).thenReturn(true);

        boolean result = authService.validateCredentials("user1", "rawPass");

        assertTrue(result);
    }

    @Test
    void validateCredentials_ShouldReturnFalse_WhenPasswordsDoNotMatch() {
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("user1");
        user.setPassword("hashedPass");

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "hashedPass")).thenReturn(false);

        boolean result = authService.validateCredentials("user1", "wrongPass");

        assertFalse(result);
    }

    @Test
    void validateCredentials_ShouldReturnFalse_WhenUserDoesNotExist() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        boolean result = authService.validateCredentials("ghost", "anyPass");

        assertFalse(result);
    }

    // --- Tests para register ---

    @Test
    void register_ShouldSaveUser_WithFormattedRole() {
        // Probamos que añade "ROLE_" y convierte a mayúsculas
        RegisterRequest request = new RegisterRequest("newuser", "pass123", "admin");
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("encoded123");

        authService.register(request);

        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("newuser") &&
                        user.getPassword().equals("encoded123") &&
                        user.getRole().equals("ROLE_ADMIN")
        ));
    }

    @Test
    void register_ShouldNotDoublePrefixRole_WhenAlreadyHasRolePrefix() {
        // Probamos que si ya trae ROLE_, no lo repite
        RegisterRequest request = new RegisterRequest("user2", "pass", "ROLE_USER");
        when(userRepository.findByUsername("user2")).thenReturn(Optional.empty());

        authService.register(request);

        verify(userRepository).save(argThat(user -> user.getRole().equals("ROLE_USER")));
    }

    @Test
    void register_ShouldThrowException_WhenUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("existing", "pass", "USER");
        when(userRepository.findByUsername("existing")).thenReturn(Optional.of(new UserJpaEntity()));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.register(request)
        );

        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}