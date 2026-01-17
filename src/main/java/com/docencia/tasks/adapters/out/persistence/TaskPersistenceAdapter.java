package com.docencia.tasks.adapters.out.persistence;

import org.springframework.stereotype.Component;

import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

@Component
public class TaskPersistenceAdapter implements ITaskPersistenceAdapter {

  private final TaskRepository jpaRepo;
  private final TaskMapper mapper;

  public TaskPersistenceAdapter(TaskRepository jpaRepo, TaskMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Task save(Task task) {
    TaskJpaEntity saved = jpaRepo.save(mapper.toJpa(task));
    return mapper.toDomain(saved);
  }

  @Override
  public List<Task> findAll() {
    return jpaRepo.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Task> findById(Long id) {
    return jpaRepo.findById(id).map(mapper::toDomain);
  }

  @Override
  public void deleteById(Long id) {
    jpaRepo.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepo.existsById(id);
  }
}
