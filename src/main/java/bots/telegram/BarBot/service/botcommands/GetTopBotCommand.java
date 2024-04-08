package bots.telegram.BarBot.service.botcommands;

import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.model.UserGroupRepository;
import bots.telegram.BarBot.service.BarBotSendMessageService;
import bots.telegram.BarBot.service.DataBaseManager;
import bots.telegram.BarBot.service.DataBaseManagerImpl;
import bots.telegram.BarBot.service.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetTopBotCommand implements BarBotCommand {
    private final DataBaseManager dataBaseManager;
    private final BarBotSendMessageService barBotSendMessageService;
    private List<UserGroup> userGroupList;
    StringBuilder topTenUsersByCockSize = new StringBuilder();

    public GetTopBotCommand(BarBotSendMessageService barBotSendMessageService) {
        this.barBotSendMessageService = barBotSendMessageService;
        this.dataBaseManager = SpringUtils.getBean(DataBaseManagerImpl.class);
    }

    @Override
    public void processCommand() {
        makeUserGroupList();
        sortUserGroupListInDescentOrderByCockSize();
        buildTop();
        sendMessageWithTop();
    }

    private void makeUserGroupList() {
        userGroupList = dataBaseManager.findAllUserGroupData();
    }

    private void sortUserGroupListInDescentOrderByCockSize() {
        Collections.sort(userGroupList, Comparator.comparingInt(UserGroup::getCockSize));
        Collections.reverse(userGroupList);
    }

    private void buildTop() {
        addTopTitle();
        addTenUsersFromTop();
    }

    private void addTopTitle() {
        topTenUsersByCockSize.append("Топ 10 коков планеты:\n\n");
    }

    private void addTenUsersFromTop() {
        for (int index = 0; index < 10; index++) {
            addUserTopLine(index);
        }
    }

    private void addUserTopLine(int index) {
        addTopPosition(index);
        addNickname(index);
        addCockSizeInfo(index);
    }

    private void addTopPosition(int index) {
        int topPosition = getTopPositionByIndex(index);
        topTenUsersByCockSize.append(topPosition).append(". ");
    }

    private int getTopPositionByIndex(int index) {
        return index+1;
    }

    private void addNickname(int index) {
        topTenUsersByCockSize.append(userGroupList.get(index).getNickname());
    }

    private void addCockSizeInfo(int index) {
        topTenUsersByCockSize.append(" - ");
        topTenUsersByCockSize.append(userGroupList.get(index).getCockSize());
        topTenUsersByCockSize.append(" см\n");
    }

    private void sendMessageWithTop() {
        SendMessage message = barBotSendMessageService.prepareMessage(topTenUsersByCockSize.toString());
        barBotSendMessageService.sendMessage(message);
    }
}
