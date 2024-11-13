package bots.telegram.BarBot.controller.commands;

import bots.telegram.BarBot.controller.commands.universal.*;
import bots.telegram.BarBot.utility.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class BarBotCommandsFactory {
    private final WrongBotCommand wrongBotCommand;
    private final ChangeNameBotCommand changeNameBotCommand;
    private final GetTopBotCommand getTopBotCommand;
    private final GetRatingBotCommand getRatingBotCommand;
    private final HelpBotCommand helpBotCommand;
    private final RollCockBotCommand rollCockBotCommand;
    private final GetRecordBotCommand getRecordBotCommand;
    private final GetGroupTopBotCommand getGroupTopBotCommand;

    public Stream<CommandResponse> processBotCommand(String command, Message message) {
        return switch (command) {
            case "барбот кок" -> rollCockBotCommand.getResponse(message);
            case "барбот помощь" -> helpBotCommand.getResponse(message);
            case "барбот рейтинг" -> getRatingBotCommand.getResponse(message);
            case "барбот топ" -> getTopBotCommand.getResponse(message);
            case "барбот рекорд" -> getRecordBotCommand.getResponse(message);
            case "барбот топ чатов" -> getGroupTopBotCommand.getResponse(message);
            default -> processOtherCommands(command, message);
        };
    }

    private Stream<CommandResponse> processOtherCommands(String command, Message message) {
        if (command.startsWith("барбот имя")) {
            return changeNameBotCommand.getResponse(message);
        } else {
            return wrongBotCommand.getResponse(message);
        }
    }
}
