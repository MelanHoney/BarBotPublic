package bots.telegram.BarBot.service;

import bots.telegram.BarBot.config.BarBotConfig;
import bots.telegram.BarBot.service.botcommands.BarBotCommand;
import bots.telegram.BarBot.service.botcommands.BarBotCommandsFactory;
import bots.telegram.BarBot.service.botcommands.RegistrationBotCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BarBotConfig barBotConfig;
    private final BarBotSendMessageServiceImpl barBotSendMessageService;

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
        this.barBotSendMessageService = new BarBotSendMessageServiceImpl(this);
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Начать играть"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(multipleConditionsUpdateCheck(update)) {
            barBotSendMessageService.setChatId(update.getMessage().getChatId());
            processGroupUpdate(update);
        }
    }

    private boolean multipleConditionsUpdateCheck(Update update) {
        return hasMessageAndText(update) && !isMessageFromBot(update) && isTypeGroupOrSupergroup(update);
    }

    private boolean isMessageFromBot(Update update) {
        if (update.getMessage().getFrom().getIsBot()) {
            logMessageFromBot(update.getMessage());
            return true;
        } else {
            return false;
        }
    }

    private void logMessageFromBot(Message message) {
        log.info("Bot message received: " + message.getText()
                + " From: " + message.getFrom().getUserName()
                + " id=" + message.getFrom().getId()
                + " chat: " + message.getChat().getTitle()
                + " id=" + message.getChatId()
                + " type=" + message.getChat().getType());
    }

    private boolean hasMessageAndText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private boolean isTypeGroupOrSupergroup(Update update) {
        if (update.getMessage().getChat().getType().equals("group") ||
                update.getMessage().getChat().getType().equals("supergroup")) {
            return true;
        } else {
            logMessageNotFromGroup(update.getMessage());
            sendChatIsNotGroupMessage(update);
            return false;
        }
    }

    private void logMessageNotFromGroup(Message message) {
        log.info("Bot not group message received: " + message.getText()
                + " From: " + message.getFrom().getUserName()
                + " id=" + message.getFrom().getId()
                + " chat: " + message.getChat().getTitle()
                + " id=" + message.getChatId()
                + " type=" + message.getChat().getType());
    }


    private void sendChatIsNotGroupMessage(Update update) {
        barBotSendMessageService.setChatId(update.getMessage().getChatId());
        String text = "Чтобы начать пользоваться ботом добавьте его в групповой чат.";
        SendMessage message = barBotSendMessageService.prepareMessage(text);
        barBotSendMessageService.sendMessage(message);
    }

    private void processGroupUpdate (Update update) {
        String command = update.getMessage().getText();
        String lowerCaseCommand = command.toLowerCase();

        if (lowerCaseCommand.startsWith("барбот")) {
            logSentCommand(update.getMessage());
            processSentCommand(lowerCaseCommand, update);
        }
        else if (command.equals("/start@BarCockBot")){
            logStartCommand(update.getMessage());
            processStartCommand(update.getMessage());
        }
    }

    private void logSentCommand(Message message) {
        log.info("BarBot command message received: " + message.getText()
                + " From: " + message.getFrom().getUserName()
                + " id=" + message.getFrom().getId()
                + " chat: " + message.getChat().getTitle()
                + " id=" + message.getChatId()
                + " type=" + message.getChat().getType());
    }

    private void processSentCommand(String command, Update update) {
        BarBotCommandsFactory barBotCommandsFactory = new BarBotCommandsFactory();
        BarBotCommand botCommand = barBotCommandsFactory.defineCommandAndUser(command, update.getMessage(), barBotSendMessageService);
        botCommand.processCommand();
    }

    private void logStartCommand(Message message) {
        log.info("BarBot start message received: " + message.getText()
                + " From: " + message.getFrom().getUserName()
                + " id=" + message.getFrom().getId()
                + " chat: " + message.getChat().getTitle()
                + " id=" + message.getChatId()
                + " type=" + message.getChat().getType());
    }

    private void processStartCommand(Message message) {
        BarBotCommand barBotCommand = new RegistrationBotCommand(message, barBotSendMessageService);
        barBotCommand.processCommand();
    }
}
