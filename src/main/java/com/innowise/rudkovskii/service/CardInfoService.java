package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.CardInfo;
import com.innowise.rudkovskii.exception.ResourceNotFoundException;
import com.innowise.rudkovskii.exception.ValidationException;
import com.innowise.rudkovskii.repository.CardInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CardInfoService {

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @CachePut(value = "cards", key = "#result.id")
    public CardInfo create(CardInfo cardInfo){
        if(cardInfoRepository.existsByNumber(cardInfo.getNumber())){
            throw new ValidationException("Card number already exists!");
        }
        
        CardInfo newCard = new CardInfo();
        newCard.setNumber(cardInfo.getNumber());
        newCard.setHolder(cardInfo.getHolder());
        newCard.setExpirationDate(cardInfo.getExpirationDate());
        newCard.setUser(cardInfo.getUser());

        return cardInfoRepository.save(newCard);
    }

    @Cacheable(value = "cards", key = "#id")
    public CardInfo getById(Integer id){
        return cardInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
    }

    @Cacheable(value = "cardsByNumber", key = "#number")
    public CardInfo getByNumber(String number){
        return cardInfoRepository.findByNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with number: " + number));
    }

    @Transactional(readOnly = true)
    public List<CardInfo> getAll(){
        return cardInfoRepository.findAll();
    }

    @CacheEvict(value = {"cards", "cardsByNumber"}, allEntries = true)
    public void delete(Integer id){
        if(!cardInfoRepository.existsById(id)){
            throw new ResourceNotFoundException("Card not found with id: " + id);
        }
        cardInfoRepository.deleteById(id);
    }
}