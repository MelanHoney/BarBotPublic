package bots.telegram.BarBot.controller.commands.universal.rollcock.operations;

import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.utility.CommandResponse;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Stream;

public abstract class CockOperation {
    abstract Stream<CommandResponse> executeOperation(Message message, UserChatDto dto, ChatDto chatDto);
}
