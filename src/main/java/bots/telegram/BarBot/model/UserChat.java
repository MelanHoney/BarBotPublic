package bots.telegram.BarBot.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_group")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChat {

    @EmbeddedId
    private UserChatPK pk;

    @Column(name = "cock_size")
    private int cockSize;

    @Column(name = "last_cock_update")
    private LocalDate lastCockUpdate = LocalDate.EPOCH;

    @Column(name = "nickname")
    private String nickname;
}
