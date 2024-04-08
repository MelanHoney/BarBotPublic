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
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetRatingBotCommand implements BarBotCommand {
    private final DataBaseManagerImpl dataBaseManager;
    private final BarBotSendMessageService barBotSendMessageService;
    private final List<UserGroupPK> userGroupPKS = new ArrayList<>();
    private final List<MessageEntity> messageEntityList = new ArrayList<>();
    private List <UserGroup> userGroupList;
    private final StringBuilder groupCockRating = new StringBuilder();
    private int totalCock;
    private final long neededGroupId;

    public GetRatingBotCommand(Message message, BarBotSendMessageService barBotSendMessageService) {
        this.barBotSendMessageService = barBotSendMessageService;
        this.neededGroupId = message.getChatId();
        this.totalCock = 0;
        this.dataBaseManager = SpringUtils.getBean(DataBaseManagerImpl.class);
    }

    @Override
    public void processCommand() {
        fillUserGroupListOfThisChat();
        sortUserGroupListInDescentOrderByCockSize();
        buildRating();
        sendMessageWithRating();
    }

    private void fillUserGroupListOfThisChat() {
        formUserGroupPKSListOfThisChat();
        userGroupList = dataBaseManager.findAllUserGroupDataById(userGroupPKS);
    }

    private void formUserGroupPKSListOfThisChat () {
        for (UserGroup userGroup : dataBaseManager.findAllUserGroupData()) {
            addUserIfInTheGroup(userGroup);
        }
    }

    private void addUserIfInTheGroup (UserGroup userGroup) {
        if (isUserInTheGroup(userGroup)) {
            userGroupPKS.add(userGroup.getPk());
        }
    }

    private boolean isUserInTheGroup(UserGroup userGroup) {
        Long currentGroupId = userGroup.getPk().getGroup_id();
        return currentGroupId.equals(neededGroupId);
    }

    private void sortUserGroupListInDescentOrderByCockSize() {
        Collections.sort(userGroupList, Comparator.comparingInt(UserGroup::getCockSize));
        Collections.reverse(userGroupList);
    }

    private void buildRating() {
        addRatingTitle();
        addRatingLines();
        addTotalCockSize();
    }

    private void addRatingTitle() {
        groupCockRating.append("Рейтинг коков:\n\n");
    }

    private void addRatingLines() {
        for (UserGroup userGroup : userGroupList) {
            addRatingLineWithMention(userGroup);
            updateTotalCockSize(userGroup);
        }
    }

    private void addRatingLineWithMention(UserGroup userGroup) {
        addRankingPlace(userGroup);
        addLinkMentionToEntityList(userGroup);
        addNicknameAndCockSize(userGroup);
    }

    private void addRankingPlace(UserGroup userGroup) {
        groupCockRating.append(getRatingPosition(userGroup)).append(". ");
    }

    private int getRatingPosition(UserGroup userGroup) {
        return userGroupList.indexOf(userGroup) + 1;
    }

    private void addLinkMentionToEntityList(UserGroup userGroup) {
        MessageEntity userTextLink = barBotSendMessageService.makeUserTextLink(groupCockRating.length(), userGroup);
        messageEntityList.add(userTextLink);
    }

    private void addNicknameAndCockSize(UserGroup userGroup) {
        groupCockRating.append(userGroup.getNickname());
        groupCockRating.append(" - ");
        groupCockRating.append(userGroup.getCockSize());
        groupCockRating.append(" см\n");
    }

    private void updateTotalCockSize(UserGroup userGroup) {
        totalCock += userGroup.getCockSize();
    }

    private void addTotalCockSize() {
        groupCockRating.append("\nТотальный кок: " + totalCock + " см");
    }

    private void sendMessageWithRating() {
        SendMessage message = barBotSendMessageService.prepareMessage(groupCockRating.toString(), messageEntityList);
        barBotSendMessageService.sendMessage(message);
    }
}
