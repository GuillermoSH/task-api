package com.docencia.tasks.adapters.out.persistence.impl;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.adapters.out.persistence.TaskJpaEntity;
import com.docencia.tasks.adapters.out.persistence.TaskRepository;
import com.docencia.tasks.domain.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskPersistenceAdapterTest {

  private TaskPersistenceAdapter adapter;

  @Mock
  private TaskRepository jpaRepo;

  @Mock
  private TaskMapper mapper;

  @BeforeEach
  void setUp() {
    adapter = new TaskPersistenceAdapter(jpaRepo, mapper);
  }

  @Test
  void save_ShouldMapToJpaAndReturnDomain() {
    Task task = new Task();
    TaskJpaEntity entityToSave = new TaskJpaEntity();
    TaskJpaEntity savedEntity = new TaskJpaEntity();
    Task domainResult = new Task();

    when(mapper.toJpa(task)).thenReturn(entityToSave);
    when(jpaRepo.save(entityToSave)).thenReturn(savedEntity);
    when(mapper.toDomain(savedEntity)).thenReturn(domainResult);

    Task result = adapter.save(task);

    assertNotNull(result);
    assertEquals(domainResult, result);
    verify(jpaRepo).save(entityToSave);
  }

  @Test
  void findAll_ShouldReturnMappedList() {
    TaskJpaEntity entity = new TaskJpaEntity();
    Task domain = new Task();

    when(jpaRepo.findAll()).thenReturn(List.of(entity));
    when(mapper.toDomain(entity)).thenReturn(domain);

    List<Task> result = adapter.findAll();

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    verify(jpaRepo).findAll();
  }

  @Test
  void findById_WhenFound_ShouldReturnMappedOptional() {
    TaskJpaEntity entity = new TaskJpaEntity();
    Task domain = new Task();

    when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
    when(mapper.toDomain(entity)).thenReturn(domain);

    Optional<Task> result = adapter.findById(1L);

    assertTrue(result.isPresent());
    assertEquals(domain, result.get());
  }

  @Test
  void findById_WhenNotFound_ShouldReturnEmptyOptional() {
    when(jpaRepo.findById(1L)).thenReturn(Optional.empty());

    Optional<Task> result = adapter.findById(1L);

    assertTrue(result.isEmpty());
    // El mapper no se llama si el Optional está vacío, cubriendo la rama negativa
    verify(mapper, never()).toDomain((TaskRequest) any());
  }

  @Test
  void deleteById_ShouldCallRepository() {
    adapter.deleteById(1L);
    verify(jpaRepo).deleteById(1L);
  }

  @Test
  void existsById_ShouldReturnRepositoryResponse() {
    when(jpaRepo.existsById(1L)).thenReturn(true);

    boolean result = adapter.existsById(1L);

    assertTrue(result);
    verify(jpaRepo).existsById(1L);
  }
}