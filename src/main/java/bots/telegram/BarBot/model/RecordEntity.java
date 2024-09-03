package bots.telegram.BarBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "record_entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordEntity {
    @Id
    private Long id;

    private String nickname;

    private int size;
}
