package com.docencia.tasks.adapters.out.persistence;

import org.junit.jupiter.api.Test;

import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskPersistenceAdapterTest {

  @Test
  void findAllMapsEntitiesToDomainTest() {
    TaskRepository jpaRepo = mock(TaskRepository.class);
    TaskMapper mapper = mock(TaskMapper.class);

    TaskPersistenceAdapter adapter = new TaskPersistenceAdapter(jpaRepo, mapper);

    TaskJpaEntity e = new TaskJpaEntity(1L, "t", "d", false);
    when(jpaRepo.findAll()).thenReturn(List.of(e));

    Task task = new Task(1L, "t", "d", false);
    when(mapper.toDomain(e)).thenReturn(task);

    List<Task> result = adapter.findAll();

    assertEquals(1, result.size());
    verify(jpaRepo).findAll();
    verify(mapper).toDomain(e);
  }

  @Test
  void findByIdMapsOptionalTest() {
    TaskRepository jpaRepo = mock(TaskRepository.class);
    TaskMapper mapper = mock(TaskMapper.class);
    TaskPersistenceAdapter adapter = new TaskPersistenceAdapter(jpaRepo, mapper);

    TaskJpaEntity taskEntity = new TaskJpaEntity(1L, "t", "d", false);
    when(jpaRepo.findById(1L)).thenReturn(Optional.of(taskEntity));
    when(mapper.toDomain(taskEntity)).thenReturn(new Task(1L, "t", "d", false));

    assertTrue(adapter.findById(1L).isPresent());
    verify(jpaRepo).findById(1L);
  }
}
