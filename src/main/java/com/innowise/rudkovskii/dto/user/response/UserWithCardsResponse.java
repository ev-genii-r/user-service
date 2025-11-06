package com.innowise.rudkovskii.dto.user.response;

import com.innowise.rudkovskii.dto.card.response.CardInfoResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserWithCardsResponse {

    private Integer id;

    private String name;

    private String surname;

    private String email;

    private LocalDate birthDate;

    private List<CardInfoResponse> cards;

}
