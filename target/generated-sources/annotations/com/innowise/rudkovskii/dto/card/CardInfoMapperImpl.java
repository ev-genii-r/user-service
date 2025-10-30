package com.innowise.rudkovskii.dto.card;

import com.innowise.rudkovskii.dto.card.request.CardInfoCreateRequest;
import com.innowise.rudkovskii.dto.card.response.CardInfoResponse;
import com.innowise.rudkovskii.dto.card.response.CardInfoWithUserResponse;
import com.innowise.rudkovskii.entity.CardInfo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-30T16:05:15+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class CardInfoMapperImpl implements CardInfoMapper {

    @Override
    public CardInfoResponse cardInfoToCardInfoResponse(CardInfo cardInfo) {
        if ( cardInfo == null ) {
            return null;
        }

        CardInfoResponse cardInfoResponse = new CardInfoResponse();

        return cardInfoResponse;
    }

    @Override
    public CardInfoWithUserResponse cardInfoToCardInfoWithUserResponse(CardInfo cardInfo) {
        if ( cardInfo == null ) {
            return null;
        }

        CardInfoWithUserResponse cardInfoWithUserResponse = new CardInfoWithUserResponse();

        return cardInfoWithUserResponse;
    }

    @Override
    public CardInfo toEntity(CardInfoCreateRequest cardInfoCreateRequest) {
        if ( cardInfoCreateRequest == null ) {
            return null;
        }

        CardInfo cardInfo = new CardInfo();

        return cardInfo;
    }
}
