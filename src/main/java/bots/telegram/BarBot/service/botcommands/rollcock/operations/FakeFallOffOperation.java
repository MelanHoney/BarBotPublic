package bots.telegram.BarBot.service.botcommands.rollcock.operations;

import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.service.BarBotSendMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class FakeFallOffOperation implements CockOperation{
    private final BarBotSendMessageService barBotSendMessageService;
    private final UserGroup userGroupData;
    private final CockOperation realOperation;
    private final List<MessageEntity> messageEntityList = new ArrayList<>();
    public FakeFallOffOperation(BarBotSendMessageService barBotSendMessageService, UserGroup userGroupData, CockOperation cockOperation) {
        this.barBotSendMessageService = barBotSendMessageService;
        this.userGroupData = userGroupData;
        this.realOperation = cockOperation;
    }

    @Override
    public void executeOperation() {
        sendFallOffMessage();
        waitThreeSeconds();
        sendRevealingMessage();
        executeRealOperation();
    }

    private void sendFallOffMessage() {
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

    private void waitThreeSeconds() {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRevealingMessage() {
        String text = "...или нет";
        SendMessage message = barBotSendMessageService.prepareMessage(text);
        barBotSendMessageService.sendMessage(message);
    }

    private void executeRealOperation() {
        realOperation.executeOperation();
    }
}
