package bots.telegram.BarBot.controller.commands.universal;

import bots.telegram.BarBot.controller.commands.BarBotCommand;
import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.dto.UserDto;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.service.database.ChatService;
import bots.telegram.BarBot.service.database.UserChatService;
import bots.telegram.BarBot.service.database.UserService;
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
public class RegistrationBotCommand extends BarBotCommand {
    private final UserService userService;
    private final ChatService chatService;
    private final UserChatService userChatService;
    private final SendMessageService sendMessageService;
    private Message message;
    private Long chatId;
    private Long userId;
    private UserChatDto dto;

    @Override
    public Stream<CommandResponse> getResponse(Message message) {
        initFields(message);
        if (dto == null) {
            registerUserIfNotRegistered(userId);
            registerChatIfNotRegistered(chatId);
            dto = userChatService.save(makeUserGroupFromMessage());
            return Stream.of(getSuccessfulResponse());
        } else {
            return Stream.of(getAlreadyRegisteredResponse());
        }
    }

    private void initFields(Message message) {
        this.message = message;
        chatId = message.getChatId();
        userId = message.getFrom().getId();
        this.dto = userChatService.findById(userId, chatId);
    }

    private void registerUserIfNotRegistered(Long userId) {
        if (userService.findById(userId) == null) {
            userService.save(registerUserFromMessage());
        }
    }

    private UserDto registerUserFromMessage() {
        return UserDto.builder()
                .id(userId)
                .username(message.getFrom().getFirstName())
                .surname(message.getFrom().getLastName())
                .build();
    }

    private void registerChatIfNotRegistered(Long groupId) {
        if (chatService.findById(groupId) == null) {
            chatService.save(registerChatFromMessage());
        }
    }

    private ChatDto registerChatFromMessage() {
        return ChatDto.builder()
                .id(chatId)
                .title(message.getChat().getTitle())
                .type(message.getChat().getType())
                .totalCockSize(0)
                .build();
    }

    private UserChatDto makeUserGroupFromMessage() {
        return UserChatDto.builder()
                .userId(userId)
                .groupId(chatId)
                .lastCockUpdate(LocalDate.EPOCH)
                .cockSize(0)
                .nickname(makeNickname())
                .build();
    }

    private String makeNickname() {
        if (!(message.getFrom().getLastName() == null)) {
            return "%s %s".formatted(message.getFrom().getFirstName(), message.getFrom().getLastName());
        } else {
            return message.getFrom().getFirstName();
        }
    }

    private CommandResponse getSuccessfulResponse() {
        List<MessageEntity> messageEntityList = new ArrayList<>();

        messageEntityList.add(sendMessageService.makeUserTextMention(0, dto, message));
        String text = TextResponses.SUCCESS_REGISTRATION_MESSAGE.getText()
                .formatted(dto.nickname());
        SendMessage message = sendMessageService.prepareMessage(chatId, text, messageEntityList);

        return new CommandResponse(message, 0);
    }

    private CommandResponse getAlreadyRegisteredResponse() {
        String text = TextResponses.ALREADY_REGISTERED_MESSAGE.getText();

        SendMessage message = sendMessageService.prepareMessage(chatId ,text);

        return new CommandResponse(message, 0);
    }
}
