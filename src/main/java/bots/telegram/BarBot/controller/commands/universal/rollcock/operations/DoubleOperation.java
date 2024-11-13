package bots.telegram.BarBot.controller.commands.universal.rollcock.operations;

import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.service.database.ChatService;
import bots.telegram.BarBot.service.database.RecordEntityService;
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
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DoubleOperation extends CockOperation {
    private final SendMessageService sendMessageService;
    private final UserChatService userChatService;
    private final ChatService chatService;
    private final RecordEntityService recordEntityService;
    private Message message;
    private UserChatDto dto;
    private ChatDto chatDto;
    private List<MessageEntity> messageEntityList;

    @Override
    public Stream<CommandResponse> executeOperation(Message message, UserChatDto dto, ChatDto chatDto) {
        initFields(message, dto, chatDto);
        updateUser();
        checkIfNewRecord();
        return Stream.of(makeResponseMessage());
    }


    private void initFields(Message message, UserChatDto dto, ChatDto chatDto) {
        this.message = message;
        this.dto = dto;
        this.chatDto = chatDto;
        messageEntityList = new ArrayList<>();
    }

    private void updateUser() {
        updateTotalCockSize();
        updateUserCockAndDate();
    }

    private void updateTotalCockSize() {
        chatService.save(ChatDto.builder()
                .id(chatDto.id())
                .title(chatDto.title())
                .type(chatDto.type())
                .totalCockSize(chatDto.totalCockSize() + dto.cockSize())
                .build());
    }

    private void updateUserCockAndDate() {
        dto = userChatService.save(UserChatDto.builder()
                .userId(dto.userId())
                .groupId(dto.groupId())
                .cockSize(dto.cockSize() * 2)
                .lastCockUpdate(LocalDate.now())
                .nickname(dto.nickname())
                .build());
    }

    private void checkIfNewRecord() {
        var record = recordEntityService.getRecordEntity();
        if (record.getSize() < dto.cockSize()) {
            recordEntityService.saveNewRecord(dto);
        }
    }

    private CommandResponse makeResponseMessage() {
        messageEntityList.add(sendMessageService.makeUserTextMention(0, dto, message));
        String text = buildTextWithUserData();
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(), text, messageEntityList);
        return new CommandResponse(responseMessage, 0);
    }

    private String buildTextWithUserData() {
        return "%s, твой песюн вирос в 2 раза.\nТеперь его длина %d см."
                .formatted(dto.nickname(), dto.cockSize());
    }
}
