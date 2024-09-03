package bots.telegram.BarBot.service.database;

import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.mapper.ChatMapper;
import bots.telegram.BarBot.model.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    public ChatDto findById(Long id) {
        return chatMapper.mapToDto(chatRepository.findById(id));
    }

    public List<ChatDto> findTopTenByTotalCock() {
        return chatMapper.mapToDtoList(chatRepository.findTop10ByOrderByTotalCockSizeDesc());
    }

    public ChatDto save(ChatDto group) {
        var groupEntity = chatMapper.mapToEntity(group);
        chatRepository.save(groupEntity);
        return group;
    }
}
