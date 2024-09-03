package bots.telegram.BarBot.controller.botcommands.universal;

import bots.telegram.BarBot.controller.botcommands.BarBotCommand;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.service.database.RecordEntityService;
import bots.telegram.BarBot.utility.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GetRecordBotCommand extends BarBotCommand {
    private final RecordEntityService recordEntityService;
    private final SendMessageService sendMessageService;

    @Override
    public Stream<CommandResponse> getResponse(Message message) {
        return Stream.of(makeRecordMessage(message));
    }

    private CommandResponse makeRecordMessage(Message message) {
        var record = recordEntityService.getRecordEntity();
        String text = "Величайший кок всех времен:\n\n%s - %d см.".formatted(record.getNickname(), record.getSize());
        var responseMessage = sendMessageService.prepareMessage(message.getChatId(), text);
        return new CommandResponse(responseMessage, 0);
    }
}
