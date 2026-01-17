package com.docencia.tasks.adapters.in.controller;

import org.junit.jupiter.api.Test;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.in.controller.TaskController;
import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.business.ITaskService;
import com.docencia.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

  @Test
  void getAll_returnsMappedResponses() {
    ITaskService service = mock(ITaskService.class);
    TaskMapper mapper = mock(TaskMapper.class);
    TaskController controller = new TaskController(service, mapper);

    Task task = new Task(1L, "a", "b", false);
    when(service.getAll()).thenReturn(List.of(task));
    when(mapper.toResponse(task)).thenReturn(new TaskResponse(1L, "a", "b", false));

    List<TaskResponse> res = controller.getAll();

    assertEquals(1, res.size());
    assertEquals(1L, res.get(0).getId());
    verify(service).getAll();
    verify(mapper).toResponse(task);
  }

  @Test
  void getById_returns404_whenNotFound() {
    ITaskService service = mock(ITaskService.class);
    TaskMapper mapper = mock(TaskMapper.class);
    TaskController controller = new TaskController(service, mapper);

    when(service.getById(10L)).thenReturn(Optional.empty());

    var resp = controller.getById(10L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void create_returns201_andBody() {
    ITaskService service = mock(ITaskService.class);
    TaskMapper mapper = mock(TaskMapper.class);
    TaskController controller = new TaskController(service, mapper);

    TaskRequest req = new TaskRequest();
    req.setTitle("t");
    req.setDescription("d");
    req.setCompleted(false);

    Task domain = new Task(null, "t", "d", false);
    Task saved = new Task(1L, "t", "d", false);

    when(mapper.toDomain(req)).thenReturn(domain);
    when(service.create(domain)).thenReturn(saved);
    when(mapper.toResponse(saved)).thenReturn(new TaskResponse(1L, "t", "d", false));

    var resp = controller.create(req);

    assertEquals(201, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals(1L, resp.getBody().getId());
  }
}
