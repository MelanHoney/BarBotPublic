package bots.telegram.BarBot.service;

import bots.telegram.BarBot.model.UserGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.List;

public interface BarBotSendMessageService {
    void sendMessage(SendMessage sendMessage);
    SendMessage prepareMessage(String text);
    SendMessage prepareMessage(String text, List<MessageEntity> messageEntityList);
    MessageEntity makeUserTextLink(int offset, UserGroup userGroupData);
}
