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
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.lang.Thread.sleep;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BarBotConfig barBotConfig;

    final String helpMessage = """
            Канал с новостями - @BarBotCock
            Для регистрации в боте напиши /start@BarCockBot
            
            Команды барбота:
            
            Барбот помощь - справка о командах бота
            Барбот кок - крутануть ваш кок
            Барбот рейтинг - рейтинг коков в чате
            Барбот топ - глобальный рейтинг коков
            Барбот имя <новое имя> - изменить свое имя в боте
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
                    try {
                        updateCockCheck(update.getMessage());
                    } catch (InterruptedException e) {

                    }
                    break;
                case "барбот помощь":
                    prepareMessage(update.getMessage().getChatId(), helpMessage);
                    break;
                case "барбот рейтинг":
                    getUserGroupTop(update);
                    break;
                case "барбот топ":
                    getUsersTop(update);
                    break;
                default:
                    if (lowerText.startsWith("барбот имя")) {
                        changeUserName(update.getMessage());
                    } else {
                        prepareMessage(update.getMessage().getChatId(), "Я не знаю такую команду. Напиши барбот помощь чтобы получить список доступных команд.");
                    }
            }
        }
        else {
            if (text.equals("/start@BarCockBot")) {
                registerUserGroup(update.getMessage());
            }
        }
    }

    private void updateUserGroupData(Message message) {
        User user = userRepository.findById(message.getFrom().getId()).get();
        Group group = groupRepository.findById(message.getChatId()).get();
        UserGroupPK userGroupPK = new UserGroupPK();
        userGroupPK.setGroup_id(message.getChatId());
        userGroupPK.setUser_id(message.getFrom().getId());
        UserGroup userGroup = userGroupRepository.findById(userGroupPK).get();

        if(!Objects.equals(user.getName(), message.getFrom().getFirstName())) {

            var userName = message.getFrom().getFirstName();

            user.setName(userName);

            userRepository.save(user);
        }
        if(!Objects.equals(user.getSecond_name(), message.getFrom().getLastName())) {

            var userLastName = message.getFrom().getLastName();

            user.setSecond_name(userLastName);

            userRepository.save(user);
        }
        if(userGroup.getNickname() == null) {

            var userNickname = "";

            if (user.getSecond_name() != null) {
                userNickname = user.getName() + " " + user.getSecond_name();
            } else {
                userNickname = user.getName();
            }
            userGroup.setNickname(userNickname);

            userGroupRepository.save(userGroup);
        }
        if(!Objects.equals(group.getTitle(), message.getChat().getTitle())) {

            var groupTitle = message.getChat().getTitle();

            group.setTitle(groupTitle);

            groupRepository.save(group);
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
            if (userRepository.findById(userGroupPK.getUser_id()).get().getSecond_name() != null) {
                userGroup.setNickname(userRepository.findById(userGroupPK.getUser_id()).get().getName() + " " + userRepository.findById(userGroupPK.getUser_id()).get().getSecond_name());
            } else {
                userGroup.setNickname(userRepository.findById(userGroupPK.getUser_id()).get().getName());
            }

            userGroupRepository.save(userGroup);

            prepareMessage(message.getChatId(), makeMention(userGroup.getPk()) + ", ты успешно зарегистрировался! \n" +
                    "Напиши барбот помощь чтобы узнать доступные команды");
        } else {
            prepareMessage(message.getChatId(), "Братанчик, ты уже зарегистрирован. Напиши барбот помощь, чтобы узнать все доступные команды.");
        }
    }

    private void updateCockCheck(Message message) throws InterruptedException {

        UserGroupPK userGroupPK = new UserGroupPK();
        userGroupPK.setUser_id(message.getFrom().getId());
        userGroupPK.setGroup_id(message.getChatId());

        if (userGroupRepository.findById(userGroupPK).isEmpty()) {
            prepareMessage(message.getChatId(), "Братанчик, ты еще не зарегистрирован. Напиши /start@BarCockBot для регистрации.");
        } else {
            updateUserGroupData(message);

            UserGroup userGroup = userGroupRepository.findById(userGroupPK).get();
            List<MessageEntity> messageEntityList = new ArrayList<>();

            messageEntityList.add(makeLinkMention(userGroupPK, 0));

            Date curTime = new Date();
            LocalDate curDate = curTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate lastUpdateDate = userGroup.getLastCockUpdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (curDate.equals(lastUpdateDate)) {
                prepareMessage(message.getChatId(), makeMention(userGroup.getPk()) + ", продовжуй играть завтра.", messageEntityList);
            } else {
                updateCock(userGroup, message);
            }

            userGroup.setLastCockUpdate(curTime);
            userGroupRepository.save(userGroup);
        }
    }

    private String randomCockOperation() {

        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        String operation = "";

        if (randomNumber < 15) {
            operation = "delete";
        } else if (randomNumber < 30) {
            operation = "fake delete";
        } else if (randomNumber < 50) {
            operation = "double";
        } else if (randomNumber < 500) {
            operation = "minus";
        } else {
            operation = "plus";
        }

        return operation;
    }

    private String randomFakeDeleteOperation() {

        Random random = new Random();
        int randomNumber = random.nextInt(100);
        String operation = "";

        if (randomNumber < 40) {
            operation = "double";
        } else if (randomNumber < 70) {
            operation = "minus";
        } else {
            operation = "plus";
        }

        return operation;
    }

    private void updateCock(UserGroup userGroup, Message message) throws InterruptedException {
        String operation = randomCockOperation();
        int lengthUpdate = randomCockSize();
        List<MessageEntity> messageEntityList = new ArrayList<>();

        messageEntityList.add(makeLinkMention(userGroup.getPk(), 0));

        switch (operation) {
            case "delete":
                userGroup.setCockSize(0);
                prepareMessage(message.getChatId(),
                        makeMention(userGroup.getPk()) + ", твой песюн видвалився!\n" +
                                "Теперь его длина 0 см.", messageEntityList);
                break;
            case "minus":
                if (userGroup.getCockSize() < lengthUpdate) {
                    userGroup.setCockSize(0);
                    prepareMessage(message.getChatId(),
                            makeMention(userGroup.getPk()) + ", твой песюн сокротився на "
                                    + lengthUpdate + " см.\n" +
                                    "Теперь его длина " + userGroup.getCockSize() + " см.", messageEntityList);
                } else {
                    userGroup.setCockSize(userGroup.getCockSize() - lengthUpdate);
                    prepareMessage(message.getChatId(),
                            makeMention(userGroup.getPk()) + ", твой песюн сокротився на "
                                    + lengthUpdate + " см.\n" +
                                    "Теперь его длина " + userGroup.getCockSize() + " см.", messageEntityList);
                }
                break;
            case "plus":
                userGroup.setCockSize(userGroup.getCockSize() + lengthUpdate);
                prepareMessage(message.getChatId(),
                        makeMention(userGroup.getPk()) + ", твой песюн вирос на "
                                + lengthUpdate + " см.\n"
                                + "Теперь его длина " + userGroup.getCockSize() + " см.", messageEntityList);
                break;
            case "fake delete":
                prepareMessage(message.getChatId(),
                        makeMention(userGroup.getPk()) + ", твой песюн видвалився!\n" +
                                "Теперь его длина 0 см.", messageEntityList);

                sleep(3000);
                prepareMessage(message.getChatId(), "...или нет");
                updateFakeDeleteCock(userGroup, message);
                break;
            case "double":
                userGroup.setCockSize(userGroup.getCockSize() * 2);
                prepareMessage(message.getChatId(),
                        makeMention(userGroup.getPk()) + ", твой песюн вирос в 2 раза!\n"
                                + "Теперь его длина " + userGroup.getCockSize() + " см.", messageEntityList);
                break;
            default:
                break;
        }
    }

    private void updateFakeDeleteCock(UserGroup userGroup, Message message) {
        String operation = randomFakeDeleteOperation();
        int lengthUpdate = randomCockSize();
        List<MessageEntity> messageEntityList = new ArrayList<>();

        messageEntityList.add(makeLinkMention(userGroup.getPk(), 0));

        switch (operation) {
            case "minus":
                if (userGroup.getCockSize() < lengthUpdate) {
                    userGroup.setCockSize(0);
                    prepareMessage(message.getChatId(),
                            makeMention(userGroup.getPk()) + ", твой песюн сокротився на "
                                    + lengthUpdate + " см.\n" +
                                    "Теперь его длина " + userGroup.getCockSize() + " см.", messageEntityList);
                } else {
                    userGroup.setCockSize(userGroup.getCockSize() - lengthUpdate);
                    prepareMessage(message.getChatId(),
                            makeMention(userGroup.getPk()) + ", твой песюн сокротився на "
                                    + lengthUpdate + " см.\n" +
                                    "Теперь его длина " + userGroup.getCockSize() + " см.", messageEntityList);
                }
                break;
            case "plus":
                userGroup.setCockSize(userGroup.getCockSize() + lengthUpdate);
                prepareMessage(message.getChatId(),
                        makeMention(userGroup.getPk()) + ", твой песюн вирос на "
                                + lengthUpdate + " см.\n"
                                + "Теперь его длина " + userGroup.getCockSize() + " см.", messageEntityList);
                break;
            case "double":
                userGroup.setCockSize(userGroup.getCockSize() * 2);
                prepareMessage(message.getChatId(),
                        makeMention(userGroup.getPk()) + ", твой песюн вирос в 2 раза!\n"
                                + "Теперь его длина " + userGroup.getCockSize() + " см.", messageEntityList);
                break;
            default:
                break;
        }
    }

    private int randomCockSize() {

        Random random = new Random();
        int lowerChance = random.nextInt(100);
        int lengthUpdate;

        if (lowerChance < 70) {
            lengthUpdate = random.nextInt(15);
        } else {
            lengthUpdate = random.nextInt(15) + 15;
        }

        return lengthUpdate;
    }

    private void getUserGroupTop(Update update) {

        List <UserGroupPK> userGroupPKS = new ArrayList<>();
        int totalCock = 0;
        int topPosition = 1;

        for (UserGroup userGroup : userGroupRepository.findAll()) {
            if (userGroup.getPk().getGroup_id().equals(update.getMessage().getChatId())) {
                userGroupPKS.add(userGroup.getPk());
            }
        }

        List <UserGroup> userGroupList = (List<UserGroup>) userGroupRepository.findAllById(userGroupPKS);
        StringBuilder userCocks = new StringBuilder();
        List<MessageEntity> messageEntityList = new ArrayList<>();

        Collections.sort(userGroupList, Comparator.comparingInt(UserGroup::getCockSize));
        Collections.reverse(userGroupList);

        userCocks.append("Рейтинг коков:\n\n");
        for (UserGroup userGroup : userGroupList) {

            userCocks.append(topPosition + ".");
            messageEntityList.add(makeLinkMention(userGroup.getPk(), userCocks.length()));
            userCocks.append(makeMention(userGroup.getPk()));
            userCocks.append(" - ");
            userCocks.append(userGroup.getCockSize());
            userCocks.append(" см\n");

            totalCock += userGroup.getCockSize();
            topPosition++;
        }
        userCocks.append("\nТотальный кок: " + totalCock + " см");

        prepareMessage(update.getMessage().getChatId(), userCocks.toString(), messageEntityList);
    }

    private void getUsersTop(Update update) {

        List <UserGroup> userGroups = (List<UserGroup>) userGroupRepository.findAll();
        int topPosition = 1;
        StringBuilder userCocks = new StringBuilder();

        Collections.sort(userGroups, Comparator.comparingInt(UserGroup::getCockSize));
        Collections.reverse(userGroups);

        userCocks.append("Топ 10 коков планеты:\n\n");
        for (int i = 0; i < 10; i++) {

            userCocks.append(topPosition).append(".");
            userCocks.append(userGroups.get(i).getNickname());
            userCocks.append(" - ");
            userCocks.append(userGroups.get(i).getCockSize());
            userCocks.append(" см\n");

            topPosition++;
        }

        prepareMessage(update.getMessage().getChatId(), userCocks.toString());
    }

    public void changeUserName(Message message) {

        UserGroupPK userGroupPK = new UserGroupPK();
        userGroupPK.setUser_id(message.getFrom().getId());
        userGroupPK.setGroup_id(message.getChatId());

        if (userGroupRepository.findById(userGroupPK).isPresent() && message.getText().length() < 250) {

            UserGroup userGroup = userGroupRepository.findById(userGroupPK).get();
            String newName = message.getText().substring(11);

            userGroup.setNickname(newName);

            userGroupRepository.save(userGroup);
            prepareMessage(message.getChatId(), "Имя успешно изменено.");
        } else {

            prepareMessage(message.getChatId(), "Братанчик, твое новое имя слишком длинное либо ты еще не зарегистрировался.");
        }
    }

    public String makeMention (UserGroupPK userGroupPK) {

        String mention;
        Long userId = userGroupPK.getUser_id();

        if (userGroupRepository.findById(userGroupPK).get().getNickname() == null) {

            mention = userRepository.findById(userId).get().getName();
        } else {

            mention = userGroupRepository.findById(userGroupPK).get().getNickname();
        }

        return mention;
    }

    public MessageEntity makeLinkMention(UserGroupPK userGroupPK, int offset) {

        MessageEntity messageEntity;
        Long userId = userGroupPK.getUser_id();

        if (userGroupRepository.findById(userGroupPK).get().getNickname() == null) {

            messageEntity = new MessageEntity("text_mention", offset, userRepository.findById(userId).get().getName().length());
            org.telegram.telegrambots.meta.api.objects.User user = new org.telegram.telegrambots.meta.api.objects.User(userId, userRepository.findById(userId).get().getName(), false);

            messageEntity.setUser(user);
        } else {

            messageEntity = new MessageEntity("text_mention", offset, userGroupRepository.findById(userGroupPK).get().getNickname().length());
            org.telegram.telegrambots.meta.api.objects.User user = new org.telegram.telegrambots.meta.api.objects.User(userId, userRepository.findById(userId).get().getName(), false);

            messageEntity.setUser(user);
        }

        return messageEntity;
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void prepareMessage(Long chatId, String text, List<MessageEntity> messageEntities) {

        SendMessage message = new SendMessage();

        message.setEntities(messageEntities);
        message.setChatId(chatId);
        message.setText(text);

        sendMessage(message);
    }

    private void prepareMessage(Long chatId, String text) {

        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(text);

        sendMessage(message);
    }
}
