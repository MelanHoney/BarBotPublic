package bots.telegram.BarBot.controller.commands.universal;

import bots.telegram.BarBot.controller.commands.BarBotCommand;
import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.service.database.ChatService;
import bots.telegram.BarBot.utility.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GetGroupTopBotCommand extends BarBotCommand {
    private final SendMessageService sendMessageService;
    private final ChatService chatService;
    private Message message;
    private List<ChatDto> topChatsDto;
    private StringBuilder responseMessage;

    @Override
    public Stream<CommandResponse> getResponse(Message message) {
        initFields(message);
        buildTop();
        return Stream.of(makeResponseMessage());
    }

    private void initFields(Message message) {
        this.message = message;
        responseMessage = new StringBuilder();
        topChatsDto = chatService.findTopTenByTotalCock();
    }

    private void buildTop() {
        responseMessage.append("Топ 10 величайших чатов:\n\n");
        for (ChatDto dto : topChatsDto) {
            responseMessage.append("%d. %s - %d см\n".
                    formatted(topChatsDto.indexOf(dto) + 1, dto.title(), dto.totalCockSize())
            );
        }
    }

    private CommandResponse makeResponseMessage() {
        SendMessage response = sendMessageService.prepareMessage(message.getChatId(), responseMessage.toString());
        return new CommandResponse(response, 0);
    }
}
