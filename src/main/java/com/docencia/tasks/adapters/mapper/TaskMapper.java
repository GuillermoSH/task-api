package com.docencia.tasks.adapters.mapper;

import org.mapstruct.*;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.out.persistence.TaskJpaEntity;
import com.docencia.tasks.domain.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  // API <-> Domain
  Task toDomain(TaskRequest request);

  TaskResponse toResponse(Task task);

  // Domain <-> JPA
  TaskJpaEntity toJpa(Task task);

  Task toDomain(TaskJpaEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(TaskRequest request, @MappingTarget Task task);
}
