package com.innowise.rudkovskii.integration;

import com.innowise.rudkovskii.config.AppConfig;
import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.exception.ResourceNotFoundException;
import com.innowise.rudkovskii.exception.ValidationException;
import com.innowise.rudkovskii.repository.UserRepository;
import com.innowise.rudkovskii.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@Testcontainers
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @BeforeAll
    static void init() {
        postgres.start();
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private User testUser;

    @BeforeEach
    void setUp() {

        redisTemplate.getConnectionFactory().getConnection().flushAll();

        if (userRepository != null) {
            userRepository.deleteAll();
        }

        testUser = new User();
        testUser.setName("Integration");
        testUser.setSurname("Test");
        testUser.setBirthDate(LocalDate.of(1985, 3, 15));
        testUser.setEmail("integration.test@example.com");
    }

    @Test
    void createUserTest() {

        User createdUser = userService.createUser(testUser);
        User retrievedUser = userService.getById(createdUser.getId());

        assertNotNull(createdUser.getId());
        assertEquals(testUser.getName(), retrievedUser.getName());
        assertEquals(testUser.getEmail(), retrievedUser.getEmail());

    }

    @Test
    void createUserExistingEmailTest() {

        userService.createUser(testUser);

        User duplicateUser = new User();
        duplicateUser.setName("Another");
        duplicateUser.setSurname("User");
        duplicateUser.setBirthDate(LocalDate.of(1990, 1, 1));
        duplicateUser.setEmail(testUser.getEmail());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(duplicateUser));

        assertEquals("Validation exception: Email already exists", exception.getMessage());
    }

    @Test
    void getAllUsersTest() {

        userService.createUser(testUser);

        User anotherUser = new User();
        anotherUser.setName("Another");
        anotherUser.setSurname("User");
        anotherUser.setBirthDate(LocalDate.of(1992, 8, 20));
        anotherUser.setEmail("another.user@example.com");
        userService.createUser(anotherUser);


        List<User> users = userService.getAll();

        assertEquals(2, users.size());
    }

    @Test
    void updateUserTest() {

        User createdUser = userService.createUser(testUser);

        User updateData = new User();
        updateData.setName("Updated Name");
        updateData.setSurname("Updated Surname");
        updateData.setBirthDate(LocalDate.of(1986, 4, 20));
        updateData.setEmail("updated.email@example.com");

        userService.updateUser(createdUser.getId(), updateData);
        User retrievedUser = userService.getById(createdUser.getId());

        assertEquals("Updated Name", retrievedUser.getName());
        assertEquals("Updated Surname", retrievedUser.getSurname());
        assertEquals("updated.email@example.com", retrievedUser.getEmail());
    }

    @Test
    void deleteUserTest() {

        User createdUser = userService.createUser(testUser);

        userService.deleteUser(createdUser.getId());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(createdUser.getId()));

        assertEquals("User with id: " + createdUser.getId() + " not found", exception.getMessage());
    }

    @Test
    void getByEmailTest() {

        User createdUser = userService.createUser(testUser);

        User foundUser = userService.getByEmail(testUser.getEmail());

        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals(testUser.getEmail(), foundUser.getEmail());
    }

}