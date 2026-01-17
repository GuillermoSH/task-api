package com.docencia.tasks.business;

import com.docencia.tasks.adapters.out.persistence.ITaskPersistenceAdapter;
import com.docencia.tasks.domain.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {

    private final ITaskPersistenceAdapter repo;

    public TaskService(ITaskPersistenceAdapter repo) {
        this.repo = repo;
    }

    @Override
    public Task create(Task task) {
        task.setId(null);
        return repo.save(task);
    }

    @Override
    public List<Task> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Task> getById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Task> update(Long id, Task patch) {
        return repo.findById(id).map(existing -> {
            if (patch.getTitle() != null) existing.setTitle(patch.getTitle());
            if (patch.getDescription() != null) existing.setDescription(patch.getDescription());
            existing.setCompleted(patch.isCompleted());
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
