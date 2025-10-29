package com.innowise.rudkovskii.dto.card.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CardInfoResponse {

    private Integer id;

    private String number;

    private String holder;

    private LocalDate expirationDate;

    private Integer userId;

}
