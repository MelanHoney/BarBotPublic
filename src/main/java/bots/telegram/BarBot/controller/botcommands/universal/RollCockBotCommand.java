package bots.telegram.BarBot.controller.botcommands.universal;

import bots.telegram.BarBot.controller.botcommands.BarBotCommand;
import bots.telegram.BarBot.controller.botcommands.universal.rollcock.OperationRandomizer;
import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.service.database.ChatService;
import bots.telegram.BarBot.service.database.UserChatService;
import bots.telegram.BarBot.utility.CommandResponse;
import bots.telegram.BarBot.utility.TextResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class RollCockBotCommand extends BarBotCommand {
    private final SendMessageService sendMessageService;
    private final UserChatService userChatService;
    private final ChatService chatService;
    private final OperationRandomizer operationRandomizer;
    private Message message;
    private List<MessageEntity> messageEntityList;
    private UserChatDto dto;
    private ChatDto chatDto;
    private LocalDate curDate;

    @Override
    public Stream<CommandResponse> getResponse(Message message) {
        initFields(message);
        if (dto != null) {
            if (!wasRolledToday()) {
                return operationRandomizer.execute(message, dto, chatDto);
            } else {
                return Stream.of(sendCockAlreadyRolledMessage());
            }
        } else {
            return Stream.of(sendNotRegisteredMessage());
        }
    }

    private void initFields(Message message) {
        this.message = message;
        messageEntityList = new ArrayList<>();
        dto = userChatService.findById(message.getFrom().getId(), message.getChatId());
        chatDto = chatService.findById(message.getChatId());
        curDate = LocalDate.now();
    }

    private boolean wasRolledToday() {
        return dto.lastCockUpdate().equals(curDate);
    }

    private CommandResponse sendCockAlreadyRolledMessage() {
        messageEntityList.add(sendMessageService.makeUserTextMention(0, dto, message));
        String text = TextResponses.COCK_ALREADY_ROLLED_TODAY_MESSAGE.getText().formatted(dto.nickname());
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(), text, messageEntityList);
        return new CommandResponse(responseMessage, 0);
    }

    private CommandResponse sendNotRegisteredMessage() {
        String text = TextResponses.NOT_REGISTERED_MESSAGE.getText();
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(), text);
        return new CommandResponse(responseMessage, 0);
    }
}
