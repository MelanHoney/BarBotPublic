package bots.telegram.BarBot.service;

import bots.telegram.BarBot.config.BarBotConfig;
import bots.telegram.BarBot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BarBotConfig barBotConfig;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    @Override
    public String getBotUsername() {
        return barBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return barBotConfig.getToken();
    }

    public TelegramBot(BarBotConfig barBotConfig) {
        this.barBotConfig = barBotConfig;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Начать играть"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {

        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getChat().getType().equals("group")) {
                processGroupUpdate(update);
            } else {
                prepareMessage(update, "To start using bot invite it to your group");
            }

        }
    }

    private void processGroupUpdate (Update update) {
        String text = update.getMessage().getText();

        if (text.startsWith("барбот")) {
            switch (text) {
                case "барбот кок":
                    break;
            }
        }
        else {
            switch (text.toLowerCase()){
                case "/start":
                    registerUserGroup(update.getMessage());
                    break;
                default:
                    break;
            }
        }
    }

    private void registerUserGroup(Message message) {
        if(userRepository.findById(message.getFrom().getId()).isEmpty()) {

            var userId = message.getFrom().getId();
            var userName = message.getFrom().getFirstName();

            User user = new User();

            user.setUserId(userId);
            user.setName(userName);

            userRepository.save(user);
        }
        if(groupRepository.findById(message.getChatId()).isEmpty()) {

            var groupId = message.getChatId();
            var groupTitle = message.getChat().getTitle();

            Group group = new Group();

            group.setGroupId(groupId);
            group.setTitle(groupTitle);

            groupRepository.save(group);
        }

        UserGroupPK userGroupPK = new UserGroupPK();

        userGroupPK.setGroup_id(message.getChatId());
        userGroupPK.setUser_id(message.getFrom().getId());

        if(userGroupRepository.findById(userGroupPK).isEmpty()) {

            UserGroup userGroup = new UserGroup();

            userGroup.setPk(userGroupPK);
            userGroup.setCockSize(0);

            userGroupRepository.save(userGroup);
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    private void prepareMessage(Update update, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(text);
        sendMessage(message);
    }

}
