package bots.telegram.BarBot.service;

import bots.telegram.BarBot.config.BarBotConfig;
import bots.telegram.BarBot.model.*;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BarBotConfig barBotConfig;

    final String helpMessage = """
            Команды барбота:
            Барбот помощь - справка о командах бота
            Барбот кок - крутануть ваш кок
            Барбот рейтинг - рейтинг коков в чате
            """;

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
            if (update.getMessage().getChat().getType().equals("group") || update.getMessage().getChat().getType().equals("supergroup")) {
                processGroupUpdate(update);
            } else {
                prepareMessage(update.getMessage().getChatId(), "Чтобы начать пользоваться ботом добавьте его в групповой чат.");
            }

        }
    }

    private void processGroupUpdate (Update update) {
        String text = update.getMessage().getText();
        String lowerText = text.toLowerCase();

        if (lowerText.startsWith("барбот")) {
            switch (lowerText) {
                case "барбот кок":
                    updateCockCheck(update.getMessage());
                    break;
                case "барбот помощь":
                    prepareMessage(update.getMessage().getChatId(), helpMessage);
                    break;
                case "барбот рейтинг":
                    getUserGroupTop(update);
                    break;
                default:
                    prepareMessage(update.getMessage().getChatId(), "Я не знаю такую команду. Напиши барбот помощь чтобы получить список доступных команд.");
            }
        }
        else {
            if (text.equals("/start@BarCockBot")) {
                registerUserGroup(update.getMessage());
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

        if(!userGroupRepository.existsById(userGroupPK)) {

            UserGroup userGroup = new UserGroup();

            userGroup.setPk(userGroupPK);
            userGroup.setCockSize(0);

            userGroupRepository.save(userGroup);

            prepareMessage(message.getChatId(), message.getFrom().getFirstName() + ", ты успешно зарегистрировался! \n" +
                    "Напиши барбот помощь чтобы узнать доступные команды");
        } else {
            prepareMessage(message.getChatId(), "Братанчик, ты уже зарегистрирован. Напиши барбот помощь, чтобы узнать все доступные команды.");
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    private void prepareMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        sendMessage(message);
    }

    private void updateCockCheck(Message message) {
        UserGroupPK userGroupPK = new UserGroupPK();

        userGroupPK.setUser_id(message.getFrom().getId());
        userGroupPK.setGroup_id(message.getChatId());

        if (userGroupRepository.findById(userGroupPK).isEmpty()) {
            prepareMessage(message.getChatId(), "Братанчик, ты еще не зарегистрирован. Напиши /start@BarCockBot для регистрации.");
        } else {
            UserGroup userGroup = userGroupRepository.findById(userGroupPK).get();

            Date curTime = new Date();
            LocalDate curDate = curTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate lastUpdateDate = userGroup.getLastCockUpdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (curDate.equals(lastUpdateDate)) {
                prepareMessage(message.getChatId(), message.getFrom().getFirstName() + ", продовжуй грати завтра.");
            } else {
                updateCock(userGroup, message);
            }
            userGroup.setLastCockUpdate(curTime);
            userGroupRepository.save(userGroup);
        }
    }

    private String randomCockOperation() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        String operation = "";

        if (randomNumber < 5) {
            operation = "delete";
        } else if (randomNumber < 45) {
            operation = "minus";
        } else {
            operation = "plus";
        }
        return operation;
    }

    private void updateCock(UserGroup userGroup, Message message) {
        String operation = randomCockOperation();

        Random random = new Random();
        int lengthUpdate = random.nextInt(30);

        switch (operation) {
            case "delete":
                userGroup.setCockSize(0);
                prepareMessage(message.getChatId(),
                        message.getFrom().getFirstName() + ", твiй песюн вiдвалився!\n" +
                                "Тепер його довжина 0 см.");
                break;
            case "minus":
                if (userGroup.getCockSize() < lengthUpdate) {
                    userGroup.setCockSize(0);
                    prepareMessage(message.getChatId(),
                            message.getFrom().getFirstName() + " твiй песюн сокротився на "
                                    + lengthUpdate + " см.\n" +
                                    "Тепер його довжина " + userGroup.getCockSize() + " см.");
                } else {
                    userGroup.setCockSize(userGroup.getCockSize() - lengthUpdate);
                    prepareMessage(message.getChatId(),
                            message.getFrom().getFirstName() + " твiй песюн сокротився на "
                                    + lengthUpdate + " см.\n" +
                                    "Тепер його довжина " + userGroup.getCockSize() + " см.");
                }
                break;
            case "plus":
                userGroup.setCockSize(userGroup.getCockSize() + lengthUpdate);
                prepareMessage(message.getChatId(),
                        message.getFrom().getFirstName() + ", твiй песюн вирiс на "
                                + lengthUpdate + " см.\n"
                                + "Тепер його довжина " + userGroup.getCockSize() + " см.");
                break;
            default:
                break;
        }
    }

    private void getUserGroupTop(Update update) {

        List <UserGroupPK> userGroupPKS = new ArrayList<>();
        for (UserGroup userGroup : userGroupRepository.findAll()) {
            if (userGroup.getPk().getGroup_id().equals(update.getMessage().getChatId())) {
                userGroupPKS.add(userGroup.getPk());
            }
        }

        List <UserGroup> userGroupList = (List<UserGroup>) userGroupRepository.findAllById(userGroupPKS);

        StringBuilder userCocks = new StringBuilder();

        userCocks.append("Рейтинг коков:\n\n");

        for (UserGroup userGroup : userGroupList) {
            userCocks.append(userRepository.findById(userGroup.getPk().getUser_id()).get().getName());
            userCocks.append(" - ");
            userCocks.append(userGroup.getCockSize());
            userCocks.append(" см\n");
        }

        prepareMessage(update.getMessage().getChatId(), userCocks.toString());
    }
}
