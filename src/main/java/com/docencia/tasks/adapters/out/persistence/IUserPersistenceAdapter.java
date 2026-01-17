package com.docencia.tasks.adapters.out.persistence;

import com.docencia.tasks.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserPersistenceAdapter {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}
