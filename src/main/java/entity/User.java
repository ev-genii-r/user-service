package entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    private int id;
    private String name;
    private String surname;
    private LocalDate birth_date;
    private String email;

}
