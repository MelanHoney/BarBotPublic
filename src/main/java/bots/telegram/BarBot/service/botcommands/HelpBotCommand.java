package bots.telegram.BarBot.service.botcommands;

import bots.telegram.BarBot.service.BarBotSendMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpBotCommand implements BarBotCommand {
    final String helpMessage = """
            Канал с новостями - @BarBotCock
            Для регистрации в боте напиши /start@BarCockBot
            
            Команды барбота:
            
            Барбот помощь - справка о командах бота
            Барбот кок - крутануть ваш кок
            Барбот рейтинг - рейтинг коков в чате
            Барбот топ - глобальный рейтинг коков
            Барбот имя <новое имя> - изменить свое имя в боте
            """;
    private final BarBotSendMessageService barBotSendMessageService;

    public HelpBotCommand(BarBotSendMessageService barBotSendMessageService) {
        this.barBotSendMessageService = barBotSendMessageService;
    }

    @Override
    public void processCommand() {
        SendMessage message = barBotSendMessageService.prepareMessage(helpMessage);
        barBotSendMessageService.sendMessage(message);
    }
}
