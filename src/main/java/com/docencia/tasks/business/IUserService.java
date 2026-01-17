package com.docencia.tasks.business;

import com.docencia.tasks.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User create(User user);
    List<User> getAll();
    Optional<User> getById(Long id);
    Optional<User> update(Long id, User patch);
    boolean delete(Long id);
}
