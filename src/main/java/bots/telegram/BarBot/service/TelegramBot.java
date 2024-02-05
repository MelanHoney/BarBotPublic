package bots.telegram.BarBot.service;

import bots.telegram.BarBot.config.BarBotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BarBotConfig barBotConfig;

    public TelegramBot(BarBotConfig barBotConfig) {
        this.barBotConfig = barBotConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

        }
    }

    @Override
    public String getBotUsername() {
        return barBotConfig.getBotName();
    }
}
