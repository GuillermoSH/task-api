package com.docencia.tasks.adapters.in.controller;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.business.ITaskService;
import com.docencia.tasks.domain.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

  private TaskController taskController;

  @Mock
  private ITaskService service;

  @Mock
  private TaskMapper mapper;

  @BeforeEach
  void setUp() {
    // Instanciación manual para evitar problemas de ApplicationContext
    taskController = new TaskController(service, mapper);
  }

  @Test
  void getAll_ShouldReturnMappedList() {
    Task task = new Task();
    TaskResponse response = new TaskResponse();

    when(service.getAll()).thenReturn(List.of(task));
    when(mapper.toResponse(task)).thenReturn(response);

    List<TaskResponse> result = taskController.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(service).getAll();
    verify(mapper).toResponse(task);
  }

  @Test
  void getById_WhenExists_ShouldReturnOk() {
    Task task = new Task();
    TaskResponse response = new TaskResponse();

    when(service.getById(1L)).thenReturn(Optional.of(task));
    when(mapper.toResponse(task)).thenReturn(response);

    ResponseEntity<TaskResponse> result = taskController.getById(1L);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
  }

  @Test
  void getById_WhenNotExists_ShouldReturnNotFound() {
    when(service.getById(1L)).thenReturn(Optional.empty());

    ResponseEntity<TaskResponse> result = taskController.getById(1L);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    assertNull(result.getBody());
  }

  @Test
  void create_ShouldReturnCreatedStatus() {
    TaskRequest request = new TaskRequest();
    Task taskDomain = new Task();
    Task savedTask = new Task();
    TaskResponse response = new TaskResponse();

    when(mapper.toDomain(request)).thenReturn(taskDomain);
    when(service.create(taskDomain)).thenReturn(savedTask);
    when(mapper.toResponse(savedTask)).thenReturn(response);

    ResponseEntity<TaskResponse> result = taskController.create(request);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
  }

  @Test
  void update_WhenExists_ShouldReturnOk() {
    TaskRequest request = new TaskRequest();
    request.setTitle("New Title");
    request.setCompleted(true);

    Task updatedTask = new Task();
    TaskResponse response = new TaskResponse();

    when(service.update(eq(1L), any(Task.class))).thenReturn(Optional.of(updatedTask));
    when(mapper.toResponse(updatedTask)).thenReturn(response);

    ResponseEntity<TaskResponse> result = taskController.update(1L, request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
  }

  @Test
  void update_WhenNotExists_ShouldReturnNotFound() {
    TaskRequest request = new TaskRequest();
    when(service.update(eq(1L), any(Task.class))).thenReturn(Optional.empty());

    ResponseEntity<TaskResponse> result = taskController.update(1L, request);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  void delete_WhenSuccess_ShouldReturnNoContent() {
    when(service.delete(1L)).thenReturn(true);

    ResponseEntity<Void> result = taskController.delete(1L);

    assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
  }

  @Test
  void delete_WhenFail_ShouldReturnNotFound() {
    when(service.delete(1L)).thenReturn(false);

    ResponseEntity<Void> result = taskController.delete(1L);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }
}