package com.docencia.tasks.business;

import com.docencia.tasks.adapters.out.persistence.ITaskPersistenceAdapter;
import com.docencia.tasks.adapters.out.persistence.IUserPersistenceAdapter;
import com.docencia.tasks.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final IUserPersistenceAdapter repo;

    public UserService(IUserPersistenceAdapter repo) {
        this.repo = repo;
    }

    @Override
    public User create(User user) {
        user.setId(null);
        return repo.save(user);
    }

    @Override
    public List<User> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public Optional<User> update(Long id, User patch) {
        return repo.findById(id).map(existing -> {
            if (patch.getUsername() != null) existing.setUsername(patch.getUsername());
            if (patch.getPassword() != null) existing.setPassword(patch.getPassword());
            return repo.save(existing);
        });
    }

    @Override
    public boolean delete(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
