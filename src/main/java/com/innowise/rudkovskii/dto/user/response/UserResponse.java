package com.innowise.rudkovskii.dto.user.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserResponse {

    private Integer id;

    private String name;

    private String surname;

    private LocalDate birthDate;

    private String email;

}
