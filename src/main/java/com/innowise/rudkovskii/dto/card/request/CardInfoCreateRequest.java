package com.innowise.rudkovskii.dto.card.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CardInfoCreateRequest {

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String number;

    @NotBlank(message = "Card holder is required")
    private String holder;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date should be in the future")
    private LocalDate expirationDate;

}
