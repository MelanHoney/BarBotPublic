package bots.telegram.BarBot.controller.commands.universal;

import bots.telegram.BarBot.controller.commands.BarBotCommand;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GetRatingBotCommand extends BarBotCommand {
    private final SendMessageService sendMessageService;
    private final UserChatService userChatService;
    private final ChatService chatService;
    private Message message;
    private List <UserChatDto> userChatDtoList;
    private List<MessageEntity> messageEntityList;
    private StringBuilder groupCockRating;

    @Override
    public Stream<CommandResponse> getResponse(Message message) {
        initFields(message);
        userChatDtoList = userChatService.findAllByChatId(message.getChatId());
        buildRating();
        return Stream.of(makeResponseMessage());
    }

    private void initFields(Message message) {
        this.message = message;
        messageEntityList = new ArrayList<>();
        groupCockRating = new StringBuilder();
    }

    private void buildRating() {
        groupCockRating.append("Рейтинг коков:\n\n");
        for (UserChatDto dto : userChatDtoList) {
            addRatingLineWithMention(dto);
        }
        addTotalCockSize();
    }

    private void addRatingLineWithMention(UserChatDto dto) {
        addRankingPlace(dto);
        addLinkMentionToEntityList(dto);
        addNicknameAndCockSize(dto);
    }

    private void addRankingPlace(UserChatDto dto) {
        groupCockRating.append("%d. "
                .formatted(userChatDtoList.indexOf(dto)+1));
    }

    private void addLinkMentionToEntityList(UserChatDto dto) {
        MessageEntity userTextLink = sendMessageService.makeUserTextMention(groupCockRating.length(), dto, message);
        messageEntityList.add(userTextLink);
    }

    private void addNicknameAndCockSize(UserChatDto dto) {
        groupCockRating.append("%s - %d см\n"
                .formatted(dto.nickname(), dto.cockSize()));
    }

    private void addTotalCockSize() {
        groupCockRating.append("\nТотальный кок: %d см".formatted(chatService.findById(message.getChatId()).totalCockSize()));
    }

    private CommandResponse makeResponseMessage() {
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(),
                groupCockRating.toString(),
                messageEntityList);
        return new CommandResponse(responseMessage, 0);
    }
}
