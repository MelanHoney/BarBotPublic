package bots.telegram.BarBot.controller.commands;

import bots.telegram.BarBot.utility.CommandResponse;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Stream;

public abstract class BarBotCommand {
    public abstract Stream<CommandResponse> getResponse(Message message);
}
