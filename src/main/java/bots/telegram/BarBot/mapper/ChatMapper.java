package bots.telegram.BarBot.mapper;

import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.model.Chat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ChatMapper implements BaseMapper<Chat, ChatDto>{
    @Override
    public ChatDto mapToDto(Optional<Chat> object) {
        return object.map(chat -> new ChatDto(
                chat.getChatId(),
                chat.getTitle(),
                chat.getType(),
                chat.getTotalCockSize()))
                .orElse(null);
    }

    @Override
    public Chat mapToEntity(ChatDto object) {
        return Chat.builder()
                .chatId(object.id())
                .title(object.title())
                .type(object.type())
                .totalCockSize(object.totalCockSize())
                .build();
    }

    @Override
    public List<ChatDto> mapToDtoList(List<Chat> objects) {
        List<ChatDto> chatDto = new ArrayList<>();

        for (Chat object : objects) {
            chatDto.add(mapToDto(Optional.ofNullable(object)));
        }

        return chatDto;
    }

    @Override
    public List<Chat> mapToEntityList(List<ChatDto> objects) {
        List<Chat> chatList = new ArrayList<>();

        for (ChatDto object : objects) {
            chatList.add(mapToEntity(object));
        }

        return chatList;
    }
}
