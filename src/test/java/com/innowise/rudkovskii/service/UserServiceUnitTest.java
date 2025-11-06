package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.exception.ResourceNotFoundException;
import com.innowise.rudkovskii.exception.ValidationException;
import com.innowise.rudkovskii.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    public void setUp() {
        testUser1 = new User();
        testUser1.setId(1);
        testUser1.setName("Leroy");
        testUser1.setSurname("Jenkins");
        testUser1.setBirthDate(LocalDate.of(1990, 1, 11));
        testUser1.setEmail("LerooooooyJenkins@example.com");

        testUser2 = new User();
        testUser2.setId(2);
        testUser2.setName("Freddie");
        testUser2.setSurname("Mercury");
        testUser2.setBirthDate(LocalDate.of(1995, 5, 15));
        testUser2.setEmail("Mama.uuuuuu@example.com");
    }

    @Test
    void createUserTest() {

        when(userRepository.existsByEmail(testUser1.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser1);

        User result = userService.createUser(testUser1);

        assertNotNull(result);
        assertEquals(testUser1.getId(), result.getId());
        assertEquals(testUser1.getEmail(), result.getEmail());
        verify(userRepository).existsByEmail(testUser1.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createExistingUserTest() {

        when(userRepository.existsByEmail(testUser1.getEmail())).thenReturn(true);


        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(testUser1));

        assertEquals("Validation exception: Email already exists", exception.getMessage());
        verify(userRepository).existsByEmail(testUser1.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getByIdTest() {

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser1));

        User result = userService.getById(1);

        assertNotNull(result);
        assertEquals(testUser1.getId(), result.getId());
        assertEquals(testUser1.getName(), result.getName());
        verify(userRepository).findById(1);

    }

    @Test
    void getByIdNotFoundTest() {

        when(userRepository.findById(333)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(333));

        assertEquals("User with id: 333 not found", exception.getMessage());
        verify(userRepository).findById(333);

    }

    @Test
    void getByEmailTest() {

        when(userRepository.findByEmail("LerooooooyJenkins@example.com")).thenReturn(Optional.of(testUser1));

        User result = userService.getByEmail("LerooooooyJenkins@example.com");

        assertNotNull(result);
        assertEquals(testUser1.getId(), result.getId());
        assertEquals(testUser1.getName(), result.getName());
        verify(userRepository).findByEmail("LerooooooyJenkins@example.com");
    }

    @Test
    void getByEmailNotFoundTest() {

        when(userRepository.findByEmail("not.existing@email.com")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getByEmail("not.existing@email.com"));

        assertEquals("User with email: not.existing@email.com not found", exception.getMessage());
        verify(userRepository).findByEmail("not.existing@email.com");

    }

    @Test
    void getAllUsersTest() {

        List<User> users = Arrays.asList(testUser1, testUser2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void updateUserTest() {

        User updatedUser = new User();
        updatedUser.setName("John");
        updatedUser.setSurname("Doe");
        updatedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        updatedUser.setEmail("john.doe@example.com");

        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.updateUser(eq(1), anyString(), anyString(), any(LocalDate.class), anyString()))
                .thenReturn(1);

        User result = userService.updateUser(1, updatedUser);

        assertNotNull(result);
        verify(userRepository).existsById(1);
        verify(userRepository).updateUser(eq(1), eq(updatedUser.getName()),
                eq(updatedUser.getSurname()), eq(updatedUser.getBirthDate()), eq(updatedUser.getEmail()));

    }

    @Test
    void updateUserNotFoundTest() {

        User updatedUser = new User();
        updatedUser.setName("John");
        updatedUser.setSurname("Doe");
        updatedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        updatedUser.setEmail("john.doe@example.com");

        when(userRepository.existsById(333)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(333, updatedUser));

        assertEquals("User with id: 333 not found", exception.getMessage());
        verify(userRepository).existsById(333);
        verify(userRepository,  never()).updateUser(eq(333), anyString(), anyString(), any(LocalDate.class), anyString());

    }

    @Test
    void updateUserEmailExistsTest() {

        User updatedUser = new User();
        updatedUser.setName("John");
        updatedUser.setSurname("Doe");
        updatedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        updatedUser.setEmail("Mama.uuuuuu@example.com");

        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.existsByEmail("Mama.uuuuuu@example.com")).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.updateUser(1, updatedUser));

        assertEquals("Validation exception: Email already exists", exception.getMessage());
        verify(userRepository).existsById(1);
        verify(userRepository).existsByEmail("Mama.uuuuuu@example.com");
        verify(userRepository,  never()).updateUser(eq(333), anyString(), anyString(), any(LocalDate.class), anyString());

    }

    @Test
    void deleteUserTest() {

        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        userService.deleteUser(1);

        verify(userRepository).existsById(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUserNotFoundTest() {
        when(userRepository.existsById(999)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(999));

        assertEquals("User with id: 999 not found", exception.getMessage());
        verify(userRepository).existsById(999);
        verify(userRepository, never()).deleteById(anyInt());
    }

}
