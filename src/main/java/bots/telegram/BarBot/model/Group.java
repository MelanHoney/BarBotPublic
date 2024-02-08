package bots.telegram.BarBot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "group_data")
@Data
public class Group {
    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "pk.group_id",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<UserGroup> userGroupList;
}
