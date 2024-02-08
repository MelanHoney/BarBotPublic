package bots.telegram.BarBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserGroupPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Long user_id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Long group_id;
}
