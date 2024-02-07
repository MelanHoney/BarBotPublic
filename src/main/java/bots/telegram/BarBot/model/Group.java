package bots.telegram.BarBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "groupsDataTable")
public class Group {
    @Id
    private Long groupId;
    private String title;
}
