package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.CardInfo;
import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.exception.ResourceNotFoundException;
import com.innowise.rudkovskii.exception.ValidationException;
import com.innowise.rudkovskii.repository.CardInfoRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardInfoServiceUnitTest {

    @Mock
    private CardInfoRepository cardInfoRepository;

    @InjectMocks
    private CardInfoService cardInfoService;

    private CardInfo testCard;
    private CardInfo testCard2;
    private User testUser;

    @BeforeEach
    void setUp() {

        testUser = new User();
        testUser.setId(1);
        testUser.setName("John");
        testUser.setSurname("Doe");
        testUser.setEmail("john.doe@example.com");

        testCard = new CardInfo();
        testCard.setId(1);
        testCard.setNumber("4111111111111111");
        testCard.setHolder("JOHN DOE");
        testCard.setExpirationDate(LocalDate.of(2025, 12, 31));
        testCard.setUser(testUser);

        testCard2 = new CardInfo();
        testCard2.setId(2);
        testCard2.setNumber("5555555555554444");
        testCard2.setHolder("JANE SMITH");
        testCard2.setExpirationDate(LocalDate.of(2026, 6, 30));
        testCard2.setUser(testUser);
    }

    @Test
    void createCardInfoTest() {

        when(cardInfoRepository.existsByNumber(testCard.getNumber())).thenReturn(false);
        when(cardInfoRepository.save(any(CardInfo.class))).thenReturn(testCard);

        CardInfo result = cardInfoService.create(testCard);

        assertNotNull(result);
        assertEquals(testCard.getId(), result.getId());
        assertEquals(testCard.getNumber(), result.getNumber());
        assertEquals(testCard.getHolder(), result.getHolder());
        verify(cardInfoRepository).existsByNumber(testCard.getNumber());
        verify(cardInfoRepository).save(any(CardInfo.class));
    }

    @Test
    void createCardInfoExistingNumberTest() {

        when(cardInfoRepository.existsByNumber(testCard.getNumber())).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> cardInfoService.create(testCard));

        assertEquals("Validation exception: Card number already exists!", exception.getMessage());
        verify(cardInfoRepository).existsByNumber(testCard.getNumber());
        verify(cardInfoRepository, never()).save(any(CardInfo.class));
    }

    @Test
    void getCardInfoByIdTest() {

        when(cardInfoRepository.findById(1)).thenReturn(Optional.of(testCard));

        CardInfo result = cardInfoService.getById(1);

        assertNotNull(result);
        assertEquals(testCard.getId(), result.getId());
        assertEquals(testCard.getNumber(), result.getNumber());
        verify(cardInfoRepository).findById(1);
    }

    @Test
    void getCardInfoByIdCardNotFoundTest() {

        when(cardInfoRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cardInfoService.getById(999));

        assertEquals("Card with id: 999 not found", exception.getMessage());
        verify(cardInfoRepository).findById(999);
    }

    @Test
    void getCardInfoByNumberTest() {

        when(cardInfoRepository.findByNumber("4111111111111111")).thenReturn(Optional.of(testCard));

        CardInfo result = cardInfoService.getByNumber("4111111111111111");

        assertNotNull(result);
        assertEquals(testCard.getNumber(), result.getNumber());
        assertEquals(testCard.getHolder(), result.getHolder());
        verify(cardInfoRepository).findByNumber("4111111111111111");
    }

    @Test
    void getCardInfoByNumberNotFoundTest() {

        when(cardInfoRepository.findByNumber("0000000000000000")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cardInfoService.getByNumber("0000000000000000"));

        assertEquals("Card with number: 0000000000000000 not found", exception.getMessage());
        verify(cardInfoRepository).findByNumber("0000000000000000");
    }

    @Test
    void getAllCardInfoTest() {

        List<CardInfo> cards = Arrays.asList(testCard, testCard2);
        when(cardInfoRepository.findAll()).thenReturn(cards);

        List<CardInfo> result = cardInfoService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cardInfoRepository).findAll();
    }

    @Test
    void deleteCardInfoTest() {

        when(cardInfoRepository.existsById(1)).thenReturn(true);
        doNothing().when(cardInfoRepository).deleteById(1);

        cardInfoService.delete(1);

        verify(cardInfoRepository).existsById(1);
        verify(cardInfoRepository).deleteById(1);
    }

    @Test
    void deleteCardInfoByIdCardNotFoundTest() {

        when(cardInfoRepository.existsById(999)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cardInfoService.delete(999));

        assertEquals("Card with id: 999 not found", exception.getMessage());
        verify(cardInfoRepository).existsById(999);
        verify(cardInfoRepository, never()).deleteById(anyInt());
    }

}