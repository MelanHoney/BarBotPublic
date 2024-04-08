package bots.telegram.BarBot.service;

import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.model.UserRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Service
public class BarBotSendMessageServiceImpl implements BarBotSendMessageService {
    private final TelegramBot telegramBot;
    @Setter
    private Long chatId;
    private final DataBaseManager dataBaseManager;

    protected BarBotSendMessageServiceImpl(TelegramBot barBot) {
        this.telegramBot = barBot;
        this.dataBaseManager = SpringUtils.getBean(DataBaseManagerImpl.class);
        System.out.println("lkj");
    }

    public SendMessage prepareMessage(String text, List<MessageEntity> messageEntities) {
        SendMessage message = new SendMessage();
        message.setEntities(messageEntities);
        message.setText(text);
        message.setChatId(chatId);
        return message;
    }

    public SendMessage prepareMessage(String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        return message;
    }

    public MessageEntity makeUserTextLink(int offset, UserGroup userGroupData) {
        Integer length = getNicknameLength(userGroupData);
        Long userId = userGroupData.getPk().getUser_id();
        String userName = getUserName(userId);

        return makeTextMentionMessageEntity(offset, length, userId, userName);
    }

    private Integer getNicknameLength(UserGroup userGroup) {
        String nickname = userGroup.getNickname();
        return nickname.length();
    }

    private String getUserName(Long userId) {
        return dataBaseManager.findUserById(userId).getName();
    }

    private MessageEntity makeTextMentionMessageEntity(int offset, Integer length, Long userId, String userName) {
        MessageEntity messageEntity = new MessageEntity("text_mention", offset, length);
        User user = new User(userId, userName, false);
        messageEntity.setUser(user);

        return messageEntity;
    }

    public void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message: "+ message.getText() + ". Error: " + e.getMessage());
        }
    }

}
