package com.docencia.tasks.business;

import com.docencia.tasks.adapters.out.persistence.IUserPersistenceAdapter;
import com.docencia.tasks.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserPersistenceAdapter repo;

    @InjectMocks
    private UserService userService;

    @Test
    void create_ShouldResetIdAndSave() {
        User input = new User();
        input.setId(99L); // ID que debería ser ignorado
        User saved = new User();
        saved.setId(1L);

        when(repo.save(any(User.class))).thenReturn(saved);

        User result = userService.create(input);

        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        verify(repo).save(argThat(user -> user.getId() == null));
    }

    @Test
    void getAll_ShouldReturnList() {
        when(repo.findAll()).thenReturn(List.of(new User()));
        List<User> result = userService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void getById_ShouldReturnOptional() {
        User user = new User();
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void getByUsername_ShouldReturnOptional() {
        when(repo.findByUsername("test")).thenReturn(Optional.of(new User()));
        assertTrue(userService.getByUsername("test").isPresent());
    }

    @Test
    void update_FullUpdate_ShouldModifyAllFields() {
        // Preparación
        User existing = new User();
        existing.setId(1L);
        existing.setUsername("old");
        existing.setPassword("oldPass");

        User patch = new User();
        patch.setUsername("new");
        patch.setPassword("newPass");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecución
        Optional<User> result = userService.update(1L, patch);

        // Verificación (Cubre los if de username y password)
        assertTrue(result.isPresent());
        assertEquals("new", result.get().getUsername());
        assertEquals("newPass", result.get().getPassword());
    }

    @Test
    void update_PartialUpdate_ShouldOnlyModifyProvidedFields() {
        User existing = new User();
        existing.setUsername("old");
        existing.setPassword("keepThis");

        User patch = new User();
        patch.setUsername("new");
        patch.setPassword(null); // No debería cambiar el password

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class))).thenReturn(existing);

        userService.update(1L, patch);

        assertEquals("new", existing.getUsername());
        assertEquals("keepThis", existing.getPassword());
    }

    @Test
    void update_NotFound_ShouldReturnEmpty() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = userService.update(1L, new User());
        assertTrue(result.isEmpty());
    }

    @Test
    void delete_Success() {
        when(repo.existsById(1L)).thenReturn(true);

        boolean result = userService.delete(1L);

        assertTrue(result);
        verify(repo).deleteById(1L);
    }

    @Test
    void delete_WhenNotExists_ShouldReturnFalse() {
        when(repo.existsById(1L)).thenReturn(false);

        boolean result = userService.delete(1L);

        assertFalse(result);
        verify(repo, never()).deleteById(anyLong());
    }
}