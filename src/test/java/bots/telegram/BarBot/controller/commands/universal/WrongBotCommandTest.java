package bots.telegram.BarBot.controller.commands.universal;

import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.utility.CommandResponse;
import bots.telegram.BarBot.utility.TextResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WrongBotCommandTest {

    WrongBotCommand wrongBotCommand;
    @Mock
    SendMessageService sendMessageService;

    @BeforeEach
    void setUp() {
        wrongBotCommand = new WrongBotCommand(sendMessageService);
    }

    @Test
    void WrongCommand_getResponse_shouldReturnStream() {
        Mockito.when(sendMessageService.prepareMessage(1L, TextResponses.WRONG_COMMAND_MESSAGE.getText())).thenReturn(new SendMessage());
        Message message = new Message();
        message.setChat(new Chat(1L, "private"));

        Stream<CommandResponse> responseStream = wrongBotCommand.getResponse(message);

        assertThat(responseStream).isNotNull();
        assertThat(responseStream.count()).isEqualTo(1);
    }
}