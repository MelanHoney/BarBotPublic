package bots.telegram.BarBot.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "user_group")
@Data
public class UserGroup {

    @EmbeddedId
    private UserGroupPK pk;

    @Column(name = "cock_size")
    private int cockSize;

    @Column(name = "last_cock_update")
    private Date lastCockUpdate = new Date(10, Calendar.JANUARY, 1);

    @Column(name = "nickname")
    private String nickname;
}
