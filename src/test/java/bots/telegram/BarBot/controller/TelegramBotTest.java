package bots.telegram.BarBot.controller;

import bots.telegram.BarBot.config.BarBotConfig;
import bots.telegram.BarBot.controller.commands.BarBotCommandsFactory;
import bots.telegram.BarBot.controller.commands.universal.RegistrationBotCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {
    @Mock
    Update update;
    @Mock
    Message message;
    @Mock
    BarBotConfig config;
    @Mock
    RegistrationBotCommand command;
    @Mock
    BarBotCommandsFactory factory;
    @InjectMocks
    TelegramBot bot;

    @Test
    void TelegramBot_onUpdateReceivedWithoutMessage_shouldNotGetMessage() {
        Mockito.when(update.hasMessage()).thenReturn(false);

        bot.onUpdateReceived(update);

        Mockito.verify(update, Mockito.never()).getMessage();
    }

    @Test
    void TelegramBot_onUpdateReceivedWithoutText_shouldNotGetText() {
        bot.onUpdateReceived(update);

        Mockito.verify(message, Mockito.never()).getText();
    }

    @Test
    void TelegramBot_onUpdateReceivedWithoutMessage_shouldNotProcessUpdate() {
        Mockito.when(update.getMessage()).thenReturn(message);

        bot.onUpdateReceived(update);

        Mockito.verify(bot, Mockito.never()).sendMessage(Mockito.any());
    }
}