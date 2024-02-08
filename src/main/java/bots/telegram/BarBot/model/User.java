package bots.telegram.BarBot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user_data")
@Data
public class User {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "pk.user_id",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<UserGroup> userGroupList;
}
