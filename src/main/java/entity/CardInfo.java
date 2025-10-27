package entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class CardInfo {

    private int id;
    private User user;
    private String number;
    private String holder;
    private LocalDate expirationDate;

    @Autowired
    public CardInfo(int id, User user, String number, String holder, LocalDate expirationDate) {
        this.id = id;
        this.user = user;
        this.number = number;
        this.holder = holder;
        this.expirationDate = expirationDate;
    }
}
