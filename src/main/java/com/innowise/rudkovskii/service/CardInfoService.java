package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.CardInfo;
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

    public void create(CardInfo cardInfo){
        cardInfoRepository.save(cardInfo);
    }

    public CardInfo findById(Integer id){
        return cardInfoRepository.findById(id).get();
    }

    public List<CardInfo> getAll(){
        return cardInfoRepository.findAll();
    }

    public void delete(Integer id){
        cardInfoRepository.deleteById(id);
    }



}
