package bots.telegram.BarBot.service.botcommands;

import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.model.UserGroupPK;
import bots.telegram.BarBot.service.BarBotSendMessageServiceImpl;
import bots.telegram.BarBot.service.DataBaseManager;
import bots.telegram.BarBot.service.DataBaseManagerImpl;
import bots.telegram.BarBot.service.SpringUtils;
import bots.telegram.BarBot.service.botcommands.rollcock.RandomOperation;
import bots.telegram.BarBot.service.botcommands.rollcock.operations.CockOperation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RollCockBotCommand implements BarBotCommand {
    private final DataBaseManager dataBaseManager;
    private final BarBotSendMessageServiceImpl barBotSendMessageService;
    private final List<MessageEntity> messageEntityList = new ArrayList<>();
    private final UserGroupPK userGroupId = new UserGroupPK();
    private UserGroup userGroupData;
    private final Date curTime;

    public RollCockBotCommand(Message message, BarBotSendMessageServiceImpl barBotSendMessageService) {
        this.barBotSendMessageService = barBotSendMessageService;
        userGroupId.setUser_id(message.getFrom().getId());
        userGroupId.setGroup_id(message.getChatId());
        curTime = new Date();
        this.dataBaseManager = SpringUtils.getBean(DataBaseManagerImpl.class);
    }

    @Override
    public void processCommand() {
        checkIfUserRegistered();
    }

    private void checkIfUserRegistered() {
        if (isUserGroupRegistered(userGroupId)) {
            userGroupData = getUserGroupDataFromRepository();
            checkLastCockRoll();
        } else {
            sendNotRegisteredMessage();
        }
    }

    private boolean isUserGroupRegistered(UserGroupPK userGroupId) {
        return dataBaseManager.isUserGroupPresent(userGroupId);
    }

    private UserGroup getUserGroupDataFromRepository() {
        return dataBaseManager.findUserGroupById(userGroupId);
    }

    private void checkLastCockRoll() {
        if (!wasRolledToday()) {
            rollCock();
        } else {
            sendCockAlreadyRolledMessage();
        }
    }

    private boolean wasRolledToday() {
        LocalDate curDate = makeLocalDateFromDate(curTime);
        LocalDate lastUpdateDate = makeLocalDateFromDate(userGroupData.getLastCockUpdate());

        return lastUpdateDate.equals(curDate);
    }

    private LocalDate makeLocalDateFromDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void rollCock() {
        CockOperation cockOperation = rollCockOperation();
        cockOperation.executeOperation();
        userGroupData.setLastCockUpdate(curTime);
        dataBaseManager.saveUserGroupData(userGroupData);
    }

    private CockOperation rollCockOperation() {
        RandomOperation randomOperation = new RandomOperation(barBotSendMessageService, userGroupData);
        int cockSize = userGroupData.getCockSize();
        return randomOperation.bySize(cockSize);
    }

    private void sendCockAlreadyRolledMessage() {
        MessageEntity userTextMention = barBotSendMessageService.makeUserTextLink(0, userGroupData);
        messageEntityList.add(userTextMention);
        String text = userGroupData.getNickname() + ", продовжуй играть завтра.";
        SendMessage message = barBotSendMessageService.prepareMessage(text, messageEntityList);
        barBotSendMessageService.sendMessage(message);
    }

    private void sendNotRegisteredMessage() {
        String text = "Братанчик, ты еще не зарегистрирован. Напиши /start@BarCockBot для регистрации.";
        SendMessage message = barBotSendMessageService.prepareMessage(text);
        barBotSendMessageService.sendMessage(message);
    }
}
