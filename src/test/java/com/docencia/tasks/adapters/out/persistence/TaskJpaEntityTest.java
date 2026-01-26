package com.docencia.tasks.adapters.out.persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskJpaEntityTest {

    @Test
    void testTaskJpaEntityAccessorsAndConstructors() {
        // 1. Test Constructor Vacío y Setters (Cubre constructor por defecto y todos los sets)
        TaskJpaEntity task = new TaskJpaEntity();
        task.setId(10L);
        task.setTitle("Estudiar Testing");
        task.setDescription("Lograr 100% cobertura");
        task.setCompleted(true);

        // Verificación de Getters
        assertEquals(10L, task.getId());
        assertEquals("Estudiar Testing", task.getTitle());
        assertEquals("Lograr 100% cobertura", task.getDescription());
        assertTrue(task.isCompleted());

        // 2. Test Constructor Completo (Cubre la inicialización por parámetros)
        TaskJpaEntity fullTask = new TaskJpaEntity(20L, "Título", "Desc", false);

        assertEquals(20L, fullTask.getId());
        assertEquals("Título", fullTask.getTitle());
        assertEquals("Desc", fullTask.getDescription());
        assertFalse(fullTask.isCompleted());
    }
}