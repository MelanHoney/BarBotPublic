package bots.telegram.BarBot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "group_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {
    @Id
    @Column(name = "group_id")
    private Long chatId;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "total_cock_size")
    private int totalCockSize;

    @OneToMany(mappedBy = "pk.groupId",
            fetch = FetchType.LAZY)
    private List<UserChat> userChatList;
}
