package com.docencia.tasks.adapters.mapper;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.out.persistence.TaskJpaEntity;
import com.docencia.tasks.domain.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperImplTest {

    private TaskMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TaskMapper.class);
    }

    @Test
    void shouldMapRequestToDomain() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Task Title");
        request.setCompleted(true);

        Task result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Task Title", result.getTitle());
        assertTrue(result.isCompleted());
    }

    @Test
    void shouldMapDomainToResponse() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Response Task");
        task.setCompleted(false);

        TaskResponse response = mapper.toResponse(task);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Response Task", response.getTitle());
        assertFalse(response.isCompleted());
    }

    @Test
    void shouldMapDomainToJpa() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("JPA Task");

        TaskJpaEntity entity = mapper.toJpa(task);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("JPA Task", entity.getTitle());
    }

    @Test
    void shouldMapJpaToDomain() {
        TaskJpaEntity entity = new TaskJpaEntity();
        entity.setId(1L);
        entity.setTitle("From DB");
        entity.setCompleted(true);

        Task task = mapper.toDomain(entity);

        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("From DB", task.getTitle());
        assertTrue(task.isCompleted());
    }

    @Test
    void shouldUpdateDomainFromRequest() {
        Task task = new Task();
        task.setTitle("Original Title");
        task.setDescription("Original Desc");

        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Title");

        mapper.updateDomainFromRequest(request, task);

        assertEquals("Updated Title", task.getTitle());
        assertEquals("Original Desc", task.getDescription());
    }

    @Test
    void shouldReturnNullWhenInputsAreNull() {
        assertNull(mapper.toDomain((TaskRequest) null));
        assertNull(mapper.toDomain((TaskJpaEntity) null));
        assertNull(mapper.toResponse(null));
        assertNull(mapper.toJpa(null));
    }

    @Test
    void shouldHandleNullUpdate() {
        Task task = new Task();
        task.setTitle("Same");

        mapper.updateDomainFromRequest(null, task);

        assertEquals("Same", task.getTitle());
    }
}