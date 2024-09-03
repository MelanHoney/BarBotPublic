package bots.telegram.BarBot.controller.botcommands.universal;

import bots.telegram.BarBot.controller.botcommands.BarBotCommand;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.utility.CommandResponse;
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
public class HelpBotCommand extends BarBotCommand {
    private final SendMessageService sendMessageService;
    final String helpMessage = """
            В барботе можно соревноваться с другими пользователями размером кока. Кок можно крутить раз в день.
            Также можно добавить бота в групповой чат и играть там с друзьями.
            
            Канал с новостями - @BarBotCock
            Чтобы начать играть в групповом чате, добавь туда бота и напиши /start@BarCockBot
            
            Команды барбота:
            
            Барбот помощь - справка о командах бота
            Барбот кок - крутануть ваш кок
            Барбот рейтинг - рейтинг коков в чате
            Барбот топ - глобальный рейтинг коков
            Барбот топ чатов - глобальный рейтинг чатов
            Барбот имя <новое имя> - изменить свое имя в боте
            Барбот рекорд - максимальный кок за все время
            """;

    @Override
    public Stream<CommandResponse> getResponse(Message message) {
        SendMessage responseMessage = sendMessageService.prepareMessage(message.getChatId(), helpMessage);
        responseMessage.setEntities(makeHelpEntityList());
        return Stream.of(new CommandResponse(responseMessage, 0));
    }

    private List<MessageEntity> makeHelpEntityList() {
        List<MessageEntity> entities = new ArrayList<>();

        entities.add(makeBoldTypeEntity(88, 10));
        entities.add(makeBoldTypeEntity(240, 16));
        entities.add(makeBoldTypeEntity(301, 13));
        entities.add(makeBoldTypeEntity(341, 10));
        entities.add(makeBoldTypeEntity(372, 14));
        entities.add(makeBoldTypeEntity(410, 10));
        entities.add(makeBoldTypeEntity(448, 16));
        entities.add(makeBoldTypeEntity(492, 22));
        entities.add(MessageEntity.builder().type(EntityType.ITALIC).offset(483+19).length(11).build());
        entities.add(makeBoldTypeEntity(542, 13));

        return entities;
    }

    private MessageEntity makeBoldTypeEntity(int offset, int length) {
        return MessageEntity.builder()
                .type(EntityType.BOLD)
                .offset(offset)
                .length(length)
                .build();
    }
}
