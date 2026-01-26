package com.docencia.tasks.adapters.in.controller;

import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.mapper.UserMapper;
import com.docencia.tasks.business.IUserService;
import com.docencia.tasks.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private UserController userController;

    @Mock
    private IUserService service;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userController = new UserController(service, mapper, passwordEncoder);
    }

    @Test
    void getAll_ReturnsList() {
        User user = new User();
        UserResponse response = new UserResponse();

        when(service.getAll()).thenReturn(List.of(user));
        when(mapper.toResponse(user)).thenReturn(response);

        List<UserResponse> result = userController.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(service).getAll();
    }

    @Test
    void getById_Found() {
        User user = new User();
        UserResponse response = new UserResponse();
        when(service.getById(1L)).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(response);

        var resp = userController.getById(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
    }

    @Test
    void getById_NotFound() {
        when(service.getById(1L)).thenReturn(Optional.empty());

        var resp = userController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void getByUsername_Found() {
        User user = new User();
        UserResponse response = new UserResponse();
        when(service.getByUsername("admin")).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(response);

        var resp = userController.getById("admin");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void getByUsername_NotFound() {
        when(service.getByUsername("ghost")).thenReturn(Optional.empty());

        var resp = userController.getById("ghost");

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void create_Returns201() {
        UserRequest request = new UserRequest();
        request.setPassword("123");
        User domain = new User();
        User created = new User();
        UserResponse response = new UserResponse();

        when(mapper.toDomain(request)).thenReturn(domain);
        when(service.create(domain)).thenReturn(created);
        when(passwordEncoder.encode("123")).thenReturn("encoded123");
        when(mapper.toResponse(created)).thenReturn(response);

        var resp = userController.create(request);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("encoded123", created.getPassword());
    }

    @Test
    void update_Found() {
        UserRequest request = new UserRequest();
        request.setUsername("new");
        request.setPassword("pass");
        User updated = new User();
        UserResponse response = new UserResponse();

        when(passwordEncoder.encode("pass")).thenReturn("hash");
        when(service.update(eq(1L), any(User.class))).thenReturn(Optional.of(updated));
        when(mapper.toResponse(updated)).thenReturn(response);

        var resp = userController.update(1L, request);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void update_NotFound() {
        UserRequest request = new UserRequest();
        when(service.update(eq(1L), any(User.class))).thenReturn(Optional.empty());

        var resp = userController.update(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void delete_Success() {
        when(service.delete(1L)).thenReturn(true);

        var resp = userController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    void delete_NotFound() {
        when(service.delete(1L)).thenReturn(false);

        var resp = userController.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}