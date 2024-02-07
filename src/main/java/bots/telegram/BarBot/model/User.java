package bots.telegram.BarBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "usersDataTable")
public class User {
    @Id
    private Long userId;
    private String name;
}
