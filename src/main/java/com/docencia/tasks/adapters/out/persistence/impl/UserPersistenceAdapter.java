package com.docencia.tasks.adapters.out.persistence.impl;

import com.docencia.tasks.adapters.mapper.UserMapper;
import com.docencia.tasks.adapters.out.persistence.IUserPersistenceAdapter;
import com.docencia.tasks.adapters.out.persistence.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.UserRepository;
import com.docencia.tasks.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserPersistenceAdapter implements IUserPersistenceAdapter {
    private final UserRepository jpaRepo;
    private final UserMapper mapper;

    public UserPersistenceAdapter(UserRepository jpaRepo, UserMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = jpaRepo.save(mapper.toJpa(user));
        return mapper.toDomain(saved);
    }

    @Override
    public List<User> findAll() {
        return jpaRepo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepo.findByUsername(username).map(mapper::toDomain);
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
