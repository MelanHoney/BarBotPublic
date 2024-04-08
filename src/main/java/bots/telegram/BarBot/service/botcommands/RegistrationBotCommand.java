package bots.telegram.BarBot.service.botcommands;

import bots.telegram.BarBot.model.Group;
import bots.telegram.BarBot.model.User;
import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.model.UserGroupPK;
import bots.telegram.BarBot.service.BarBotSendMessageService;
import bots.telegram.BarBot.service.DataBaseManager;
import bots.telegram.BarBot.service.DataBaseManagerImpl;
import bots.telegram.BarBot.service.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class RegistrationBotCommand implements BarBotCommand{
    private final DataBaseManager dataBaseManager;
    private final BarBotSendMessageService barBotSendMessageService;
    private final Message message;
    private User userData = new User();
    private Group groupData = new Group();
    private final UserGroup userGroupData = new UserGroup();

    public RegistrationBotCommand(Message message, BarBotSendMessageService barBotSendMessageService) {
        this.barBotSendMessageService = barBotSendMessageService;
        this.message = message;
        this.dataBaseManager = SpringUtils.getBean(DataBaseManagerImpl.class);
    }

    @Override
    public void processCommand() {
        registerUserIfNotRegistered(message);
        registerGroupIfNotRegistered(message);
        registerUserGroupIfNotRegistered();
    }

    private void registerUserIfNotRegistered(Message message) {
        if (!isUserRegistered(message)) {
            setUserDataFromMessage(message);
            dataBaseManager.saveUserData(userData);
        } else {
            setUserDataFromRepository();
        }
    }

    private boolean isUserRegistered(Message message) {
        return dataBaseManager.isUserPresent(message.getFrom().getId());
    }

    private void setUserDataFromMessage(Message message) {
        userData.setUserId(message.getFrom().getId());
        userData.setName(message.getFrom().getFirstName());
        userData.setSecond_name(message.getFrom().getLastName());
    }

    private void setUserDataFromRepository() {
        userData = dataBaseManager.findUserById(message.getFrom().getId());
    }

    private void registerGroupIfNotRegistered(Message message) {
        if (!isGroupRegistered(message)) {
            setGroupDataFromMessage(message);
            dataBaseManager.saveGroupData(groupData);
        } else {
            setGroupDataFromRepository();
        }
    }

    private boolean isGroupRegistered(Message message) {
        return dataBaseManager.isGroupPresent(message.getChatId());
    }

    private void setGroupDataFromMessage(Message message) {
        groupData.setGroupId(message.getChatId());
        groupData.setTitle(message.getChat().getTitle());
    }

    private void setGroupDataFromRepository() {
        groupData = dataBaseManager.findGroupById(message.getChatId());
    }

    private void registerUserGroupIfNotRegistered() {
        if (!isUserGroupRegistered()) {
            registerUserGroup();
            sendSuccessRegistrationMessage();
        } else {
            sendAlreadyRegisteredMessage();
        }
    }

    private boolean isUserGroupRegistered() {
        UserGroupPK userGroupPK = makeUserGroupPK();
        return dataBaseManager.isUserGroupPresent(userGroupPK);
    }

    private UserGroupPK makeUserGroupPK() {
        UserGroupPK userGroupPK = new UserGroupPK();
        userGroupPK.setUser_id(userData.getUserId());
        userGroupPK.setGroup_id(groupData.getGroupId());
        return userGroupPK;
    }

    private void registerUserGroup() {
        setUserGroupPkAndCockSize();
        setUserGroupNickname();
        dataBaseManager.saveUserGroupData(userGroupData);
    }

    private void setUserGroupPkAndCockSize() {
        userGroupData.setPk(makeUserGroupPK());
        userGroupData.setCockSize(0);
    }

    private void setUserGroupNickname() {
        if (hasSecondName()) {
            userGroupData.setNickname(userData.getName() + " " + userData.getSecond_name());
        } else {
            userGroupData.setNickname(userData.getName());
        }
    }

    private boolean hasSecondName() {
        return userData.getSecond_name() != null;
    }

    private void sendSuccessRegistrationMessage() {
        String text = makeSuccessRegistrationMessage();
        List<MessageEntity> messageEntityList = new ArrayList<>();
        messageEntityList.add(barBotSendMessageService.makeUserTextLink(0, userGroupData));
        SendMessage message = barBotSendMessageService.prepareMessage(text, messageEntityList);
        barBotSendMessageService.sendMessage(message);
    }

    private String makeSuccessRegistrationMessage() {
        return userGroupData.getNickname() + ", ты успешно зарегистрировался! \n" +
                "Напиши барбот помощь чтобы узнать доступные команды";
    }

    private void sendAlreadyRegisteredMessage() {
        String text = "Братанчик, ты уже зарегистрирован. Напиши барбот помощь, чтобы узнать все доступные команды.";
        SendMessage message = barBotSendMessageService.prepareMessage(text);
        barBotSendMessageService.sendMessage(message);
    }
}
