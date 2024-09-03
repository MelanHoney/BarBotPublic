package bots.telegram.BarBot.controller.botcommands.universal;

import bots.telegram.BarBot.controller.botcommands.BarBotCommand;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.service.database.UserChatService;
import bots.telegram.BarBot.utility.CommandResponse;
import bots.telegram.BarBot.utility.TextResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ChangeNameBotCommand extends BarBotCommand {
    private final SendMessageService sendMessageService;
    private final UserChatService userChatService;
    private Message message;
    private UserChatDto dto;
    private String newNickname;

    @Override
    public Stream<CommandResponse> getResponse(Message message) {
        initFields(message);
        return Stream.of(tryUpdateUsername() ? makeSuccessResponse() : makeFailResponse());
    }

    private void initFields(Message message) {
        this.message = message;
        newNickname = message.getText().substring(11);
    }

    private boolean tryUpdateUsername() {
        if (isUsernameShortEnough() && doesUserExists()) {
            updateUsername();
            return true;
        } else {
            return false;
        }
    }

    private boolean isUsernameShortEnough() {
        return newNickname.length() < 40;
    }

    private boolean doesUserExists() {
        dto = userChatService.findById(message.getFrom().getId(), message.getChatId());
        return dto != null;
    }

    private void updateUsername() {
        UserChatDto newDto = UserChatDto.builder()
                .userId(dto.userId())
                .groupId(dto.groupId())
                .cockSize(dto.cockSize())
                .lastCockUpdate(dto.lastCockUpdate())
                .nickname(newNickname)
                .build();
        userChatService.save(newDto);
    }

    private CommandResponse makeSuccessResponse() {
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(), TextResponses.CHANGE_NAME_SUCCESS_MESSAGE.getText());

        return new CommandResponse(responseMessage, 0);
    }

    private CommandResponse makeFailResponse() {
        SendMessage responseMessage = defineFailMessage();

        return new CommandResponse(responseMessage, 0);
    }

    private SendMessage defineFailMessage() {
        if (!isUsernameShortEnough()) {
            return sendMessageService.prepareMessage(message.getChatId(), TextResponses.TOO_LONG_NICKNAME_MESSAGE.getText());
        } else {
            return sendMessageService.prepareMessage(message.getChatId(), TextResponses.NOT_REGISTERED_MESSAGE.getText());
        }
    }
}
