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
import java.util.Random;

public class MinusOperation implements CockOperation{
    DataBaseManager dataBaseManager;
    private final BarBotSendMessageService barBotSendMessageService;
    private final UserGroup userGroupData;
    private final int cockSizeBeforeUpdate;
    private int randomReducedSize;
    private final List<MessageEntity> messageEntityList = new ArrayList<>();

    public MinusOperation(BarBotSendMessageService barBotSendMessageService, UserGroup userGroupData) {
        this.barBotSendMessageService = barBotSendMessageService;
        this.userGroupData = userGroupData;
        this.cockSizeBeforeUpdate = userGroupData.getCockSize();
        this.dataBaseManager = SpringUtils.getBean(DataBaseManagerImpl.class);
    }

    @Override
    public void executeOperation() {
        updateUserCockSize();
        sendMessage();
    }

    private void updateUserCockSize() {
        randomReducedSize = randomIntBetweenFiveAndThirty();
        updateCockSizeCheck();
        dataBaseManager.saveUserGroupData(userGroupData);
    }

    private int randomIntBetweenFiveAndThirty() {
        Random size = new Random();
        return size.nextInt(5, 30);
    }

    private void updateCockSizeCheck() {
        if (randomReducedSize > cockSizeBeforeUpdate) {
            setZeroCockSize();
        } else {
            setReducedCockSize();
        }
    }

    private void setZeroCockSize() {
        userGroupData.setCockSize(0);
    }

    private void setReducedCockSize() {
        userGroupData.setCockSize(cockSizeBeforeUpdate - randomReducedSize);
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
                ", твой песюн сокротився на " +
                randomReducedSize + " см.\n" +
                "Теперь его длина " +
                userGroupData.getCockSize() +
                " см.";
    }
}
