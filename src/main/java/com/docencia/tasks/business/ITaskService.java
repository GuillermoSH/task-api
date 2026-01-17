package com.docencia.tasks.business;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.Task;

public interface ITaskService {
  Task create(Task task);
  List<Task> getAll();
  Optional<Task> getById(Long id);
  Optional<Task> update(Long id, Task patch);
  boolean delete(Long id);
}
