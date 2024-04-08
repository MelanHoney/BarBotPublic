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

public class DoubleOperation implements CockOperation{
    private final DataBaseManager dataBaseManager;
    private final BarBotSendMessageService barBotSendMessageService;
    private final UserGroup userGroupData;
    private final List<MessageEntity> messageEntityList = new ArrayList<>();

    public DoubleOperation(BarBotSendMessageService barBotSendMessageService, UserGroup userGroupData) {
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
        doubleCockSize();
        dataBaseManager.saveUserGroupData(userGroupData);
    }

    private void doubleCockSize() {
        int cockSizeBeforeUpdate = userGroupData.getCockSize();
        userGroupData.setCockSize(cockSizeBeforeUpdate * 2);
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
                ", твой песюн вирос в 2 раза!\n" +
                "Теперь его длина " +
                userGroupData.getCockSize() +
                " см.";
    }
}
