package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.CardInfo;
import com.innowise.rudkovskii.exception.ResourceNotFoundException;
import com.innowise.rudkovskii.exception.ValidationException;
import com.innowise.rudkovskii.repository.CardInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CardInfoService {

    @Autowired
    CardInfoRepository cardInfoRepository;

    public CardInfo create(CardInfo cardInfo){
        if(cardInfoRepository.existsByNumber(cardInfo.getNumber())){
            throw new ValidationException("Card number already exists!");
        }
        return cardInfoRepository.save(cardInfo);
    }

    public CardInfo getById(Integer id){
        return cardInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
    }

    public CardInfo getByNumber(String number){
        return cardInfoRepository.findByNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with number: " + number));
    }

    public List<CardInfo> getAll(){
        return cardInfoRepository.findAll();
    }

    public void delete(Integer id){
        if(!cardInfoRepository.existsById(id)){
            throw new ResourceNotFoundException("Card not found with id: " + id);
        }
        cardInfoRepository.deleteById(id);
    }



}
