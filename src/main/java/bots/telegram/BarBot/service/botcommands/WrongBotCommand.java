package bots.telegram.BarBot.service.botcommands;

import bots.telegram.BarBot.service.BarBotSendMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WrongBotCommand implements BarBotCommand {
    final String wrongCommandMessage = "Я не знаю такую команду. Напиши барбот помощь чтобы получить список доступных команд.";
    private final BarBotSendMessageService barBotSendMessageService;

    public WrongBotCommand(BarBotSendMessageService barBotSendMessageService) {
        this.barBotSendMessageService = barBotSendMessageService;
    }

    @Override
    public void processCommand() {
        SendMessage message = barBotSendMessageService.prepareMessage(wrongCommandMessage);
        barBotSendMessageService.sendMessage(message);
    }
}
