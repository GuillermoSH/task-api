package com.docencia.tasks.infraestructure.auth.service;

import com.docencia.tasks.adapters.out.persistence.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.UserRepository;
import com.docencia.tasks.infraestructure.auth.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateCredentials(String username, String password) {
        System.out.println("Intentando validar usuario: " + username);

        return userRepository.findByUsername(username)
                .map(user -> {
                    boolean matches = passwordEncoder.matches(password, user.getPassword());
                    System.out.println("Usuario encontrado en BD: " + user.getUsername());
                    System.out.println("Password en BD: " + user.getPassword());
                    System.out.println("¿La contraseña coincide?: " + matches);
                    return matches;
                })
                .orElseGet(() -> {
                    System.out.println("Usuario NO encontrado en la base de datos.");
                    return false;
                });
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        UserJpaEntity user = new UserJpaEntity();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        String formattedRole = request.role().startsWith("ROLE_") ?
                request.role() : "ROLE_" + request.role().toUpperCase();
        user.setRole(formattedRole);

        userRepository.save(user);
    }
}
