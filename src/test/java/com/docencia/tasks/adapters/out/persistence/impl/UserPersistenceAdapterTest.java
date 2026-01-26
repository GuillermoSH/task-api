package com.docencia.tasks.adapters.out.persistence.impl;

import com.docencia.tasks.adapters.mapper.UserMapper;
import com.docencia.tasks.adapters.out.persistence.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.UserRepository;
import com.docencia.tasks.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

    private UserPersistenceAdapter adapter;

    @Mock
    private UserRepository jpaRepo;

    @Mock
    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        adapter = new UserPersistenceAdapter(jpaRepo, mapper);
    }

    @Test
    void save_ShouldMapToJpaAndReturnDomain() {
        User user = new User();
        UserJpaEntity entity = new UserJpaEntity();
        UserJpaEntity savedEntity = new UserJpaEntity();
        User domainResult = new User();

        when(mapper.toJpa(user)).thenReturn(entity);
        when(jpaRepo.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(domainResult);

        User result = adapter.save(user);

        assertNotNull(result);
        verify(jpaRepo).save(entity);
    }

    @Test
    void findAll_ShouldReturnMappedList() {
        UserJpaEntity entity = new UserJpaEntity();
        User domain = new User();

        when(jpaRepo.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<User> result = adapter.findAll();

        assertEquals(1, result.size());
        verify(jpaRepo).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnMappedOptional() {
        UserJpaEntity entity = new UserJpaEntity();
        User domain = new User();

        when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<User> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        verify(jpaRepo).findById(1L);
    }

    @Test
    void findByUsername_WhenExists_ShouldReturnMappedOptional() {
        UserJpaEntity entity = new UserJpaEntity();
        User domain = new User();

        when(jpaRepo.findByUsername("test")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<User> result = adapter.findByUsername("test");

        assertTrue(result.isPresent());
        verify(jpaRepo).findByUsername("test");
    }

    @Test
    void deleteById_ShouldInvokeJpaRepo() {
        adapter.deleteById(1L);
        verify(jpaRepo).deleteById(1L);
    }

    @Test
    void existsById_ShouldReturnBoolean() {
        when(jpaRepo.existsById(1L)).thenReturn(true);

        boolean exists = adapter.existsById(1L);

        assertTrue(exists);
        verify(jpaRepo).existsById(1L);
    }
}