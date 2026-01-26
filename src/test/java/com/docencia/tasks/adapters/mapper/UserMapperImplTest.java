package com.docencia.tasks.adapters.mapper;

import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.out.persistence.UserJpaEntity;
import com.docencia.tasks.domain.model.User;
import org.h2.engine.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperImplTest {
    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void shouldMapRequestToDomain() {
        UserRequest request = new UserRequest();
        request.setUsername("ghernandez");
        request.setPassword("secret123");
        request.setRole("ADMIN");

        User result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals("ghernandez", result.getUsername());
        assertEquals("secret123", result.getPassword());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void shouldMapDomainToResponse() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user.test");
        user.setRole("USER");

        UserResponse response = mapper.toResponse(user);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("user.test", response.getUsername());
        assertEquals("USER", response.getRole());
    }

    @Test
    void shouldMapDomainToJpa() {
        User user = new User();
        user.setId(99L);
        user.setUsername("db.admin");

        UserJpaEntity entity = mapper.toJpa(user);

        assertNotNull(entity);
        assertEquals(99L, entity.getId());
        assertEquals("db.admin", entity.getUsername());
    }

    @Test
    void shouldMapJpaToDomain() {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(5L);
        entity.setUsername("persistence.user");
        entity.setPassword("hashed_pass");

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(5L, user.getId());
        assertEquals("persistence.user", user.getUsername());
        assertEquals("hashed_pass", user.getPassword());
    }

    @Test
    void shouldUpdateDomainFromRequestIgnoringNulls() {
        User user = new User();
        user.setUsername("old.user");
        user.setRole("USER");

        UserRequest request = new UserRequest();
        request.setUsername("new.user");
        // role y password son null en el request

        mapper.updateDomainFromRequest(request, user);

        assertEquals("new.user", user.getUsername());
        assertEquals("USER", user.getRole()); // Mantiene el original
    }

    @Test
    void shouldReturnNullWhenInputsAreNull() {
        assertNull(mapper.toDomain((UserRequest) null));
        assertNull(mapper.toDomain((UserJpaEntity) null));
        assertNull(mapper.toResponse(null));
        assertNull(mapper.toJpa(null));
    }

    @Test
    void shouldHandleNullUpdateInput() {
        User user = new User();
        user.setUsername("keep.me");

        mapper.updateDomainFromRequest(null, user);

        assertEquals("keep.me", user.getUsername());
    }
}