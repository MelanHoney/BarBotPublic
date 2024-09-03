package bots.telegram.BarBot.controller.botcommands.universal;

import bots.telegram.BarBot.controller.botcommands.BarBotCommand;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.utility.CommandResponse;
import bots.telegram.BarBot.utility.TextResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class WrongBotCommand extends BarBotCommand {
    private final SendMessageService sendMessageService;

    @Override
    public Stream<CommandResponse> getResponse (Message message) {
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(),
                TextResponses.WRONG_COMMAND_MESSAGE.getText());
        responseMessage.setEntities(makeMessageEntities());
        return Stream.of(new CommandResponse(responseMessage, 0));
    }

    private List<MessageEntity> makeMessageEntities() {
        List<MessageEntity> entities = new ArrayList<>();
        entities.add(MessageEntity.builder()
                        .offset(32)
                        .type(EntityType.BOLD)
                        .length(13)
                .build());
        return entities;
    }
}
