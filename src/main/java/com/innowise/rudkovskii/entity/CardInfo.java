package com.innowise.rudkovskii.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table
@Entity(name = "card_info")

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "number")
    private String number;

    @Column(name = "holder")
    private String holder;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;
}
