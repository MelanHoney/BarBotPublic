package bots.telegram.BarBot.controller.botcommands.universal;

import bots.telegram.BarBot.controller.botcommands.BarBotCommand;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.service.database.UserChatService;
import bots.telegram.BarBot.utility.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GetTopBotCommand extends BarBotCommand {
    private final SendMessageService sendMessageService;
    private final UserChatService userChatService;
    private Message message;
    private List<UserChatDto> userChatDtoTop;
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
        userChatDtoTop = userChatService.findTop10Cocks();
    }

    private void buildTop() {
        responseMessage.append("Топ 10 коков планеты:\n\n");
        addTenUsersFromTop();
    }

    private void addTenUsersFromTop() {
        for (UserChatDto dto : userChatDtoTop) {
            addUserTopLine(dto);
        }
    }

    private void addUserTopLine(UserChatDto dto) {
        responseMessage.append("%d. %s - %d см\n".
                formatted(userChatDtoTop.indexOf(dto) + 1, dto.nickname(), dto.cockSize())
        );
    }

    private CommandResponse makeResponseMessage() {
        SendMessage response = sendMessageService.prepareMessage(message.getChatId(), responseMessage.toString());
        return new CommandResponse(response, 0);
    }
}
