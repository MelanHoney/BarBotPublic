package bots.telegram.BarBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChatPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Long groupId;
}
