package com.innowise.rudkovskii.controller;

import com.innowise.rudkovskii.dto.card.CardInfoMapper;
import com.innowise.rudkovskii.dto.card.request.CardInfoCreateRequest;
import com.innowise.rudkovskii.dto.card.response.CardInfoResponse;
import com.innowise.rudkovskii.entity.CardInfo;
import com.innowise.rudkovskii.service.CardInfoService;
import com.innowise.rudkovskii.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cards")
public class CardInfoController {

    private final CardInfoService cardInfoService;
    private final CardInfoMapper cardInfoMapper;

    public CardInfoController(CardInfoService cardInfoService, CardInfoMapper cardInfoMapper) {
        this.cardInfoService = cardInfoService;
        this.cardInfoMapper = cardInfoMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardInfoResponse> getCardInfo(@PathVariable Integer id){
        CardInfo card = cardInfoService.getById(id);
        CardInfoResponse cardInfoResponse = cardInfoMapper.cardInfoToCardInfoResponse(card);
        return ResponseEntity.ok(cardInfoResponse);
    }

    @GetMapping("/{number}")
    public ResponseEntity<CardInfoResponse> getCardInfo(@PathVariable String number){
        CardInfo card = cardInfoService.getByNumber(number);
        CardInfoResponse cardInfoResponse = cardInfoMapper.cardInfoToCardInfoResponse(card);
        return ResponseEntity.ok(cardInfoResponse);
    }

    @PostMapping
    public ResponseEntity<CardInfoResponse> createCard(@Valid @RequestBody CardInfoCreateRequest  cardInfoCreateRequest){
        CardInfo card = cardInfoMapper.toEntity(cardInfoCreateRequest);
        CardInfo createdCard = cardInfoService.create(card);
        CardInfoResponse cardInfoResponse = cardInfoMapper.cardInfoToCardInfoResponse(createdCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardInfoResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void>  deleteCardInfo(@RequestParam Integer id){
        cardInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
