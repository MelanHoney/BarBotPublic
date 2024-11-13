package bots.telegram.BarBot.service;

import bots.telegram.BarBot.dto.UserChatDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SendMessageServiceTest {

    SendMessageService sendMessageService;

    @BeforeEach
    void setUp() {
        sendMessageService = new SendMessageService();
    }

    @Test
    void SendMessageService_prepareMessage_shouldReturnMessage() {
        Long chatId = 1L;
        String text = "test";

        var sendMessage = sendMessageService.prepareMessage(chatId, text);

        assertThat(sendMessage.getChatId()).isEqualTo("1");
        assertThat(sendMessage.getText()).isEqualTo("test");
        assertThat(sendMessage).isInstanceOf(SendMessage.class);
    }

    @Test
    void SendMessageService_prepareMessage_shouldReturnMessageWithEntities() {
        Long chatId = 1L;
        String text = "test";
        List<MessageEntity> entities = List.of(new MessageEntity(), new MessageEntity());

        var sendMessage = sendMessageService.prepareMessage(chatId, text, entities);

        assertThat(sendMessage.getChatId()).isEqualTo("1");
        assertThat(sendMessage.getText()).isEqualTo("test");
        assertThat(sendMessage.getEntities()).isEqualTo(entities);
        assertThat(sendMessage).isInstanceOf(SendMessage.class);
    }

    @Test
    void SendMessageService_makeUserTextMention_shouldReturnMessageWithMention() {
        int offset = 2;
        UserChatDto dto = UserChatDto.builder().nickname("dummy").build();
        var user = new User();
        Message message = new Message();
        message.setFrom(user);

        var userTextMention = sendMessageService.makeUserTextMention(offset, dto, message);

        assertThat(userTextMention.getOffset()).isEqualTo(offset);
        assertThat(userTextMention.getLength()).isEqualTo(dto.nickname().length());
        assertThat(userTextMention.getUser()).isEqualTo(user);
        assertThat(userTextMention.getType()).isEqualTo(EntityType.TEXTMENTION);
        assertThat(userTextMention).isInstanceOf(MessageEntity.class);
    }
}