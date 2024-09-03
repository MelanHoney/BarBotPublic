package bots.telegram.BarBot.controller.botcommands.universal.rollcock.operations;

import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.service.database.ChatService;
import bots.telegram.BarBot.service.database.UserChatService;
import bots.telegram.BarBot.utility.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MinusOperation extends CockOperation {
    private final SendMessageService sendMessageService;
    private final UserChatService userChatService;
    private final ChatService chatService;
    private Message message;
    private UserChatDto dto;
    private ChatDto chatDto;
    private List<MessageEntity> messageEntityList;
    private int randomReducedSize;


    @Override
    public Stream<CommandResponse> executeOperation(Message message, UserChatDto dto, ChatDto chatDto) {
        initFields(message, dto, chatDto);
        rollAndUpdateUser();
        return Stream.of(makeResponseMessage());
    }


    private void initFields(Message message, UserChatDto dto, ChatDto chatDto) {
        this.message = message;
        this.dto = dto;
        this.chatDto = chatDto;
        messageEntityList = new ArrayList<>();
    }

    private void rollAndUpdateUser() {
        randomReducedSize = randomIntBetweenFiveAndThirty();
        updateUserAndGroup();
    }

    private int randomIntBetweenFiveAndThirty() {
        Random size = new Random();
        return size.nextInt(5, 30);
    }

    private void updateUserAndGroup() {
        if (randomReducedSize > dto.cockSize()) {
            updateTotalCockSize(dto.cockSize());
            updateCockSizeAndLastUpdate(0);
        } else {
            updateTotalCockSize(randomReducedSize);
            updateCockSizeAndLastUpdate(dto.cockSize() - randomReducedSize);
        }
    }

    private void updateTotalCockSize(int size) {
        chatService.save(ChatDto.builder()
                .id(chatDto.id())
                .title(chatDto.title())
                .type(chatDto.type())
                .totalCockSize(chatDto.totalCockSize() - size)
                .build());
    }

    private void updateCockSizeAndLastUpdate(int size) {
        dto = userChatService.save(UserChatDto.builder()
                .userId(dto.userId())
                .groupId(dto.groupId())
                .cockSize(size)
                .lastCockUpdate(LocalDate.now())
                .nickname(dto.nickname())
                .build());
    }

    private CommandResponse makeResponseMessage() {
        messageEntityList.add(sendMessageService.makeUserTextMention(0, dto, message));
        String text = buildTextWithUserData();
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(), text, messageEntityList);
        return new CommandResponse(responseMessage, 0);
    }

    private String buildTextWithUserData() {
        return "%s, твой песюн сокротився на %d см.\nТеперь его длина %d см."
                .formatted(dto.nickname(), randomReducedSize, dto.cockSize());
    }
}
