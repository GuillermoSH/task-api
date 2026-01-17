package com.docencia.tasks.adapters.mapper;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.out.persistence.TaskJpaEntity;
import com.docencia.tasks.adapters.out.persistence.UserJpaEntity;
import com.docencia.tasks.domain.model.Task;
import com.docencia.tasks.domain.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

  // API <-> Domain
  User toDomain(UserRequest request);

  UserResponse toResponse(User user);

  // Domain <-> JPA
  UserJpaEntity toJpa(User user);

  User toDomain(UserJpaEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(UserRequest request, @MappingTarget User user);
}
