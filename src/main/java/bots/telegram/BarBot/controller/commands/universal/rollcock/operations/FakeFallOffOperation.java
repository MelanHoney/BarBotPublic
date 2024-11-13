package bots.telegram.BarBot.controller.commands.universal.rollcock.operations;

import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.utility.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class FakeFallOffOperation {
    private final SendMessageService sendMessageService;
    private Message message;
    private UserChatDto dto;
    private ChatDto chatDto;
    private List<MessageEntity> messageEntityList;
    private CockOperation realOperation;

    public Stream<CommandResponse> executeOperation(Message message, UserChatDto dto, ChatDto chatDto, CockOperation realOperation) {
        initFields(message, dto, chatDto, realOperation);
        return makeResponseMessagesWithDelay();
    }

    private void initFields(Message message, UserChatDto dto, ChatDto chatDto, CockOperation realOperation) {
        this.message = message;
        this.dto = dto;
        this.chatDto = chatDto;
        messageEntityList = new ArrayList<>();
        this.realOperation = realOperation;
    }

    private Stream<CommandResponse> makeResponseMessagesWithDelay() {
        var stream = Stream.of(makeFallOffMessage(), makeRevealingMessage());
        return Stream.concat(stream, updateUserAndGetRealResponse());
    }

    private CommandResponse makeFallOffMessage() {
        messageEntityList.add(sendMessageService.makeUserTextMention(0, dto, message));
        String text = buildTextWithUserData();
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(), text, messageEntityList);
        return new CommandResponse(responseMessage, 3000);
    }

    private String buildTextWithUserData() {
        return "%s, твой песюн видвалився!\nТеперь его длина 0 см."
                .formatted(dto.nickname());
    }

    private CommandResponse makeRevealingMessage() {
        String text = "...или нет";
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(), text);
        return new CommandResponse(responseMessage, 1000);
    }

    private Stream<CommandResponse> updateUserAndGetRealResponse() {
        return realOperation.executeOperation(message, dto, chatDto);
    }
}
