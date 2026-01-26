package com.docencia.tasks.adapters.in.controller;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.mapper.UserMapper;
import com.docencia.tasks.adapters.out.persistence.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.UserRepository;
import com.docencia.tasks.business.IUserService;
import com.docencia.tasks.domain.model.Task;
import com.docencia.tasks.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users API")
@CrossOrigin
public class UserController {

    private final IUserService service;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserController(IUserService service, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @Operation(summary = "Get all users (Admin only)")
    public List<UserResponse> getAll() {
        return service.getAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username")
    public ResponseEntity<UserResponse> getById(@PathVariable String username) {
        return service.getByUsername(username)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a user")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        User created = service.create(mapper.toDomain(request));
        created.setPassword(passwordEncoder.encode(request.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user (partial)")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserRequest request) {
        User patch = new User();
        patch.setUsername(request.getUsername());
        patch.setPassword(passwordEncoder.encode(request.getPassword()));

        return service.update(id, patch)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}