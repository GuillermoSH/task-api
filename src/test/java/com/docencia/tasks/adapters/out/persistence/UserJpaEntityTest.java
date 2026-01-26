package com.docencia.tasks.adapters.out.persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserJpaEntityTest {

    @Test
    void testConstructorsAndGettersSetters() {
        // Test constructor vacío y setters
        UserJpaEntity user1 = new UserJpaEntity();
        user1.setId(1L);
        user1.setUsername("admin");
        user1.setPassword("pass");
        user1.setRole("ROLE_ADMIN");

        assertEquals(1L, user1.getId());
        assertEquals("admin", user1.getUsername());
        assertEquals("pass", user1.getPassword());
        assertEquals("ROLE_ADMIN", user1.getRole());

        // Test constructor con ID
        UserJpaEntity user2 = new UserJpaEntity(2L);
        assertEquals(2L, user2.getId());

        // Test constructor completo
        UserJpaEntity user3 = new UserJpaEntity(3L, "user", "123", "ROLE_USER");
        assertEquals(3L, user3.getId());
        assertEquals("user", user3.getUsername());
        assertEquals("123", user3.getPassword());
        assertEquals("ROLE_USER", user3.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        UserJpaEntity userA = new UserJpaEntity(1L);
        UserJpaEntity userB = new UserJpaEntity(1L);
        UserJpaEntity userC = new UserJpaEntity(2L);
        String notAnEntity = "I am a string";

        // Caso: Misma referencia
        assertTrue(userA.equals(userA));

        // Caso: Mismo ID (equals debe ser true)
        assertTrue(userA.equals(userB));
        assertEquals(userA.hashCode(), userB.hashCode());

        // Caso: Distinto ID
        assertFalse(userA.equals(userC));
        assertNotEquals(userA.hashCode(), userC.hashCode());

        // Caso: Nulo y Distinta Clase (Cubre las ramas del if inicial de equals)
        assertFalse(userA.equals(null));
        assertFalse(userA.equals(notAnEntity));
    }
}