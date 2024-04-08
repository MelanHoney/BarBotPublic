package bots.telegram.BarBot.service.botcommands.rollcock.operations;

import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.model.UserGroupRepository;
import bots.telegram.BarBot.service.BarBotSendMessageService;
import bots.telegram.BarBot.service.DataBaseManager;
import bots.telegram.BarBot.service.DataBaseManagerImpl;
import bots.telegram.BarBot.service.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class FallOffOperation implements CockOperation{
    DataBaseManager dataBaseManager;
    private final BarBotSendMessageService barBotSendMessageService;
    private final UserGroup userGroupData;
    private final List<MessageEntity> messageEntityList = new ArrayList<>();

    public FallOffOperation(BarBotSendMessageService barBotSendMessageService, UserGroup userGroupData) {
        this.barBotSendMessageService = barBotSendMessageService;
        this.userGroupData = userGroupData;
        this.dataBaseManager = SpringUtils.getBean(DataBaseManagerImpl.class);
    }

    @Override
    public void executeOperation() {
        updateUserCockSize();
        sendMessage();
    }

    private void updateUserCockSize() {
        setZeroCockSize();
        dataBaseManager.saveUserGroupData(userGroupData);
    }

    private void setZeroCockSize() {
        userGroupData.setCockSize(0);
    }

    private void sendMessage() {
        SendMessage message = prepareMessage();
        barBotSendMessageService.sendMessage(message);
    }

    private SendMessage prepareMessage() {
        MessageEntity userTextMention = barBotSendMessageService.makeUserTextLink(0, userGroupData);
        messageEntityList.add(userTextMention);
        String text = buildTextWithUserData();
        return barBotSendMessageService.prepareMessage(text, messageEntityList);
    }

    private String buildTextWithUserData() {
        return userGroupData.getNickname() +
                ", твой песюн видвалився!\n" +
                "Теперь его длина 0 см.";
    }
}
