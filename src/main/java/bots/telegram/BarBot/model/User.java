package bots.telegram.BarBot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "second_name")
    private String secondName;

    @OneToMany(mappedBy = "pk.userId",
            fetch = FetchType.LAZY)
    private List<UserChat> userChatList;
}
