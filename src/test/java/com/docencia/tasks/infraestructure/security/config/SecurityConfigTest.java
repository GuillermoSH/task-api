package com.docencia.tasks.infraestructure.security.config;

import com.docencia.tasks.adapters.out.persistence.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.UserRepository;
import com.docencia.tasks.infraestructure.security.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtAuthenticationFilter jwtFilter;

    @Mock
    private HttpSecurity http;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void shouldProvideBCryptPasswordEncoder() {
        var encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void userDetailsServiceShouldLoadUserWhenItExists() {
        UserDetailsService service = securityConfig.userDetailsService(userRepository);
        UserJpaEntity entity = new UserJpaEntity();
        entity.setUsername("tester");
        entity.setPassword("encoded_pass");
        entity.setRole("USER");

        when(userRepository.findByUsername("tester")).thenReturn(Optional.of(entity));

        UserDetails user = service.loadUserByUsername("tester");
        assertEquals("tester", user.getUsername());
    }

    @Test
    void userDetailsServiceShouldThrowExceptionWhenUserNotFound() {
        UserDetailsService service = securityConfig.userDetailsService(userRepository);
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("ghost"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void securityFilterChainShouldConfigureHttpSecurity() throws Exception {
        // Configuramos el mock para que cada llamada al "builder" devuelva el mismo objeto http
        // Esto evita que el encadenamiento de métodos rompa la referencia del objeto.
        when(http.csrf(any())).thenReturn(http);
        when(http.headers(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.httpBasic(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);

        securityConfig.securityFilterChain(http, jwtFilter);

        // Ahora la verificación funcionará porque es el mismo objeto
        verify(http).addFilterBefore(eq(jwtFilter), eq(UsernamePasswordAuthenticationFilter.class));
        verify(http).build();
    }
}