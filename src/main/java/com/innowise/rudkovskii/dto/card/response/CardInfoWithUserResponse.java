package com.innowise.rudkovskii.dto.card.response;

import com.innowise.rudkovskii.dto.user.response.UserResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CardInfoWithUserResponse {

    private Integer id;

    private String number;

    private String holder;

    private LocalDate expirationDate;

    private UserResponse user;

}
