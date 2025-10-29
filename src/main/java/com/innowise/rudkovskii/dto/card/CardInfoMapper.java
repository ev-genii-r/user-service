package com.innowise.rudkovskii.dto.card;

import com.innowise.rudkovskii.dto.card.request.CardInfoCreateRequest;
import com.innowise.rudkovskii.dto.card.response.CardInfoResponse;
import com.innowise.rudkovskii.dto.card.response.CardInfoWithUserResponse;
import com.innowise.rudkovskii.entity.CardInfo;
import com.innowise.rudkovskii.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardInfoMapper {

    CardInfoResponse cardInfoToCardInfoResponse(CardInfo cardInfo);

    @Mapping(target = "user", source = "user")
    CardInfoWithUserResponse cardInfoToCardInfoWithUserResponse(CardInfo cardInfo);

    CardInfo toEntity(CardInfoCreateRequest cardInfoCreateRequest);

}
