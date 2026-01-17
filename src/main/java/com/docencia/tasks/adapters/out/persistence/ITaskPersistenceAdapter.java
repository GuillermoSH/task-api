package com.docencia.tasks.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.Task;

public interface ITaskPersistenceAdapter {
  Task save(Task task);
  List<Task> findAll();
  Optional<Task> findById(Long id);
  void deleteById(Long id);
  boolean existsById(Long id);
}
