package com.innowise.rudkovskii.integration;

import com.innowise.rudkovskii.config.AppConfig;
import com.innowise.rudkovskii.entity.CardInfo;
import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.exception.ResourceNotFoundException;
import com.innowise.rudkovskii.exception.ValidationException;
import com.innowise.rudkovskii.repository.CardInfoRepository;
import com.innowise.rudkovskii.repository.UserRepository;
import com.innowise.rudkovskii.service.CardInfoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.*;
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
class CardInfoServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CardInfoService cardInfoService;

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private CardInfo testCard;

    @BeforeAll
    static void init() {
        postgres.start();
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
    }

    @BeforeEach
    void setUp() {

        redisTemplate.getConnectionFactory().getConnection().flushAll();
        cardInfoRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setName("Integration");
        testUser.setSurname("Test");
        testUser.setBirthDate(LocalDate.of(1985, 3, 15));
        testUser.setEmail("integration.test@example.com");
        testUser = userRepository.save(testUser);

        testCard = new CardInfo();
        testCard.setNumber("4111111111111111");
        testCard.setHolder("INTEGRATION TEST");
        testCard.setExpirationDate(LocalDate.of(2025, 12, 31));
        testCard.setUser(testUser);
    }

    @Test
    void createCardInfoTest() {

        CardInfo createdCard = cardInfoService.create(testCard);
        CardInfo retrievedCard = cardInfoService.getById(createdCard.getId());

        assertEquals(testCard.getNumber(), retrievedCard.getNumber());
        assertEquals(testCard.getHolder(), retrievedCard.getHolder());
        assertEquals(testCard.getExpirationDate(), retrievedCard.getExpirationDate());
        assertNotNull(retrievedCard.getUser());
        assertEquals(testUser.getId(), retrievedCard.getUser().getId());
    }

    @Test
    void createCardInfoNumberExistsTest() {

        cardInfoService.create(testCard);

        CardInfo duplicateCard = new CardInfo();
        duplicateCard.setNumber(testCard.getNumber());
        duplicateCard.setHolder("DIFFERENT HOLDER");
        duplicateCard.setExpirationDate(LocalDate.of(2026, 6, 30));
        duplicateCard.setUser(testUser);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> cardInfoService.create(duplicateCard));

        assertEquals("Validation exception: Card number already exists!", exception.getMessage());
    }

    @Test
    void getAllCardsTest() {

        cardInfoService.create(testCard);

        CardInfo anotherCard = new CardInfo();
        anotherCard.setNumber("5555555555554444");
        anotherCard.setHolder("ANOTHER CARD");
        anotherCard.setExpirationDate(LocalDate.of(2026, 8, 20));
        anotherCard.setUser(testUser);
        cardInfoService.create(anotherCard);

        List<CardInfo> cards = cardInfoService.getAll();

        assertEquals(2, cards.size());
        assertTrue(cards.stream().anyMatch(card -> card.getNumber().equals("4111111111111111")));
        assertTrue(cards.stream().anyMatch(card -> card.getNumber().equals("5555555555554444")));
    }

    @Test
    void getCardInfoByNumberTest() {

        CardInfo createdCard = cardInfoService.create(testCard);

        CardInfo foundCard = cardInfoService.getByNumber(testCard.getNumber());

        assertNotNull(foundCard);
        assertEquals(createdCard.getId(), foundCard.getId());
        assertEquals(testCard.getNumber(), foundCard.getNumber());
        assertEquals(testCard.getHolder(), foundCard.getHolder());
    }

    @Test
    void getByNumberCardInfoNotFoundTest() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cardInfoService.getByNumber("0000000000000000"));

        assertEquals("Card with number: 0000000000000000 not found", exception.getMessage());
    }

    @Test
    void deleteCardInfoTest() {

        CardInfo createdCard = cardInfoService.create(testCard);

        CardInfo foundCard = cardInfoService.getById(createdCard.getId());
        assertNotNull(foundCard);

        cardInfoService.delete(createdCard.getId());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cardInfoService.getById(createdCard.getId()));

        assertEquals("Card with id: " + createdCard.getId() + " not found", exception.getMessage());
    }

    @Test
    void deleteCardInfoNotFoundTest() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cardInfoService.delete(999));

        assertEquals("Card with id: 999 not found", exception.getMessage());
    }

    @Test
    void createCardInfoWithDifferentUsersTest() {

        User anotherUser = new User();
        anotherUser.setName("Another");
        anotherUser.setSurname("User");
        anotherUser.setBirthDate(LocalDate.of(1990, 1, 1));
        anotherUser.setEmail("another.user@example.com");
        anotherUser = userRepository.save(anotherUser);

        CardInfo cardForUser1 = new CardInfo();
        cardForUser1.setNumber("4111111111111111");
        cardForUser1.setHolder("USER 1 CARD");
        cardForUser1.setExpirationDate(LocalDate.of(2025, 12, 31));
        cardForUser1.setUser(testUser);

        CardInfo cardForUser2 = new CardInfo();
        cardForUser2.setNumber("5555555555554444");
        cardForUser2.setHolder("USER 2 CARD");
        cardForUser2.setExpirationDate(LocalDate.of(2026, 6, 30));
        cardForUser2.setUser(anotherUser);

        CardInfo createdCard1 = cardInfoService.create(cardForUser1);
        CardInfo createdCard2 = cardInfoService.create(cardForUser2);

        assertNotNull(createdCard1);
        assertNotNull(createdCard2);
        assertEquals(testUser.getId(), createdCard1.getUser().getId());
        assertEquals(anotherUser.getId(), createdCard2.getUser().getId());

        List<CardInfo> allCards = cardInfoService.getAll();
        assertEquals(2, allCards.size());
    }
}