package com.innowise.rudkovskii.dto.user.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserUpdateRequest {

    private String name;

    private String surname;

    @Past(message = "Birth should be earlier than now")
    private LocalDate birthDate;

    @Email(message = "Email should be valid")
    private String email;

}
