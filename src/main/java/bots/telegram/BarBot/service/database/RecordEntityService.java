package bots.telegram.BarBot.service.database;

import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.model.RecordEntity;
import bots.telegram.BarBot.model.RecordEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecordEntityService {
    private final RecordEntityRepository recordEntityRepository;

    public RecordEntity getRecordEntity() {
        return recordEntityRepository.findById(1L).orElse(null);
    }

    public void saveNewRecord(UserChatDto dto) {
        var newRecord = RecordEntity.builder()
                .id(1L)
                .nickname(dto.nickname())
                .size(dto.cockSize())
                .build();
        recordEntityRepository.save(newRecord);
    }
}
