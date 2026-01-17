package com.docencia.tasks.adapters.in.controller.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.docencia.tasks.adapters.out.persistence.ITaskPersistenceAdapter;
import com.docencia.tasks.business.TaskService;
import com.docencia.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  ITaskPersistenceAdapter repo;

  @InjectMocks
  TaskService service;

  @Test
  void create_setsIdNull_andSaves() {
    Task input = new Task(99L, "t", "d", false);

    when(repo.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

    Task created = service.create(input);

    ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
    verify(repo).save(captor.capture());

    assertNull(captor.getValue().getId(), "TaskService debe poner id a null al crear");
    assertEquals("t", created.getTitle());
  }

  @Test
  void getAll_delegatesToRepo() {
    when(repo.findAll()).thenReturn(List.of(new Task(1L, "a", "b", false)));
    List<Task> all = service.getAll();
    assertEquals(1, all.size());
    verify(repo).findAll();
  }

  @Test
  void getById_returnsOptional() {
    when(repo.findById(1L)).thenReturn(Optional.of(new Task(1L, "a", "b", false)));
    assertTrue(service.getById(1L).isPresent());
    verify(repo).findById(1L);
  }

  @Test
  void update_mergesTitleAndDescription_andUpdatesCompleted() {
    Task existing = new Task(1L, "old", "oldDesc", false);
    when(repo.findById(1L)).thenReturn(Optional.of(existing));
    when(repo.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

    Task patch = new Task(null, "new", null, true);

    Task updated = service.update(1L, patch).orElseThrow();

    assertEquals("new", updated.getTitle());
    assertEquals("oldDesc", updated.getDescription(), "Si patch.description es null, se mantiene la anterior");
    assertTrue(updated.isCompleted());
    verify(repo).save(any(Task.class));
  }

  @Test
  void delete_returnsFalse_whenNotExists() {
    when(repo.existsById(5L)).thenReturn(false);
    assertFalse(service.delete(5L));
    verify(repo, never()).deleteById(anyLong());
  }

  @Test
  void delete_deletes_whenExists() {
    when(repo.existsById(5L)).thenReturn(true);
    assertTrue(service.delete(5L));
    verify(repo).deleteById(5L);
  }
}
