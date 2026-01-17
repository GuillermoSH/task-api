package com.docencia.tasks.adapters.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.business.ITaskService;
import com.docencia.tasks.domain.model.Task;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks API")
@CrossOrigin
public class TaskController {

  private final ITaskService service;
  private final TaskMapper mapper;

  public TaskController(ITaskService service, TaskMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(summary = "Get all tasks")
  public List<TaskResponse> getAll() {
    return service.getAll().stream().map(mapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get task by id")
  public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
    return service.getById(id)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create task")
  public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
    Task created = service.create(mapper.toDomain(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update task (partial)")
  public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody TaskRequest request) {
    // convert request -> domain patch: completed may be null; title/desc may be null
    Task patch = new Task();
    patch.setTitle(request.getTitle());
    patch.setDescription(request.getDescription());
    patch.setCompleted(Boolean.TRUE.equals(request.getCompleted()));

    return service.update(id, patch)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete task")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    boolean deleted = service.delete(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
