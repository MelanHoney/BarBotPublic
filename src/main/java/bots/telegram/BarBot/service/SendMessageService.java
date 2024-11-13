package bots.telegram.BarBot.service;

import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.service.database.UserChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SendMessageService {

    public SendMessage prepareMessage(Long chatId, String text, List<MessageEntity> messageEntities) {
        return SendMessage.builder()
                .entities(messageEntities)
                .text(text)
                .chatId(chatId)
                .build();
    }

    public SendMessage prepareMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    public MessageEntity makeUserTextMention(int offset, UserChatDto dto, Message message) {
        return MessageEntity.builder()
                .type(EntityType.TEXTMENTION)
                .offset(offset)
                .length(dto.nickname().length())
                .user(message.getFrom())
                .build();
    }
}
