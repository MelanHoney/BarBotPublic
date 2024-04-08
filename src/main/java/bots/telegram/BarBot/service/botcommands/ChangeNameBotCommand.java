package bots.telegram.BarBot.service.botcommands;

import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.model.UserGroupPK;
import bots.telegram.BarBot.service.BarBotSendMessageService;
import bots.telegram.BarBot.service.DataBaseManager;
import bots.telegram.BarBot.service.DataBaseManagerImpl;
import bots.telegram.BarBot.service.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ChangeNameBotCommand implements BarBotCommand {
    private final DataBaseManager dataBaseManager;
    private final BarBotSendMessageService barBotSendMessageService;
    private final UserGroupPK userGroupPK = new UserGroupPK();
    private String username;

    public ChangeNameBotCommand(Message message, BarBotSendMessageService barBotSendMessageService) {
        this.barBotSendMessageService = barBotSendMessageService;
        initNewUsername(message);
        initSenderData(message);
        this.dataBaseManager = SpringUtils.getBean(DataBaseManagerImpl.class);
    }

    private void initNewUsername(Message message) {
        username = message.getText().substring(11);
    }

    private void initSenderData(Message message) {
        userGroupPK.setUser_id(message.getFrom().getId());
        userGroupPK.setGroup_id(message.getChatId());
    }

    @Override
    public void processCommand() {
        if (tryUpdateUsername()) {
            sendSuccessMessage();
        } else {
            sendFailMessage();
        }
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
        return username.length() < 40;
    }

    private boolean doesUserExists() {
        return dataBaseManager.isUserGroupPresent(userGroupPK);
    }

    private void updateUsername() {
        UserGroup userGroup = dataBaseManager.findUserGroupById(userGroupPK);
        userGroup.setNickname(username);
        dataBaseManager.saveUserGroupData(userGroup);
    }

    private void sendSuccessMessage() {
        SendMessage message = barBotSendMessageService.prepareMessage("Имя успешно изменено.");
        barBotSendMessageService.sendMessage(message);
    }

    private void sendFailMessage() {
        SendMessage message = defineFailMessage();
        barBotSendMessageService.sendMessage(message);
    }

    private SendMessage defineFailMessage() {
        if (!isUsernameShortEnough()) {
            return barBotSendMessageService.prepareMessage("Братанчик, твое новое имя слишком длинное");
        } else {
            return barBotSendMessageService.prepareMessage("Братанчик, ты еще не зарегистрирован. Напиши /start@BarCockBot для регистрации.");
        }
    }
}
