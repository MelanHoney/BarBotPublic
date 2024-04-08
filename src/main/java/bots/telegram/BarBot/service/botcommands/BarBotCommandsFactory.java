package bots.telegram.BarBot.service.botcommands;

import bots.telegram.BarBot.service.BarBotSendMessageServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

public class BarBotCommandsFactory {
    public BarBotCommand defineCommandAndUser(String command, Message message, BarBotSendMessageServiceImpl barBotSendMessageServiceImpl) {
        BarBotCommand barBotCommand;

        switch (command) {
            case "барбот кок":
                barBotCommand = new RollCockBotCommand(message, barBotSendMessageServiceImpl);
                break;
            case "барбот помощь":
                barBotCommand = new HelpBotCommand(barBotSendMessageServiceImpl);
                break;
            case "барбот рейтинг":
                barBotCommand = new GetRatingBotCommand(message, barBotSendMessageServiceImpl);
                break;
            case "барбот топ":
                barBotCommand = new GetTopBotCommand(barBotSendMessageServiceImpl);
                break;
            default:
                if (command.startsWith("барбот имя")) {
                    barBotCommand = new ChangeNameBotCommand(message, barBotSendMessageServiceImpl);
                } else {
                    barBotCommand = new WrongBotCommand(barBotSendMessageServiceImpl);
                }
        }

        return barBotCommand;
    }
}
