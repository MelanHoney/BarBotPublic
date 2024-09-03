package bots.telegram.BarBot.controller;

import bots.telegram.BarBot.config.BarBotConfig;
import bots.telegram.BarBot.controller.botcommands.BarBotCommandsFactory;
import bots.telegram.BarBot.controller.botcommands.universal.RegistrationBotCommand;
import bots.telegram.BarBot.service.SendMessageService;
import bots.telegram.BarBot.utility.CommandResponse;
import bots.telegram.BarBot.utility.LoggingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BarBotConfig barBotConfig;
    private final SendMessageService sendMessageService;
    private final RegistrationBotCommand registrationBotCommand;
    private final BarBotCommandsFactory barBotCommandsFactory;
    private Message message;

    @Override
    public String getBotUsername() {
        return barBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return barBotConfig.getToken();
    }

    public TelegramBot(BarBotConfig barBotConfig, SendMessageService sendMessageService, RegistrationBotCommand registrationBotCommand, BarBotCommandsFactory barBotCommandsFactory) {
        this.barBotConfig = barBotConfig;
        this.sendMessageService = sendMessageService;
        this.registrationBotCommand = registrationBotCommand;
        this.barBotCommandsFactory = barBotCommandsFactory;
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
        this.message = update.getMessage();
        if(hasMessageAndText(update) && !isMessageFromBot(update)) {
            var type = update.getMessage().getChat().getType();
            if (type.equals("group") || type.equals("supergroup")) {
                processGroupUpdate(update).forEach(this::sendMessage);
            } else if(type.equals("private")) {
                processPrivateChatUpdate(update).forEach(this::sendMessage);
            } else {
                LoggingUtil.logMessageNotFromChat(update.getMessage());
            }
        }
    }

    private boolean isMessageFromBot(Update update) {
        if (update.getMessage().getFrom().getIsBot()) {
            LoggingUtil.logMessageFromBot(update.getMessage());
            return true;
        } else {
            return false;
        }
    }

    private boolean hasMessageAndText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private Stream<CommandResponse> processGroupUpdate (Update update) {
        String command = update.getMessage().getText();
        String lowerCaseCommand = command.toLowerCase();

        if (lowerCaseCommand.startsWith("барбот")) {
            LoggingUtil.logSentCommand(update.getMessage());
            return barBotCommandsFactory.processBotCommand(lowerCaseCommand, update.getMessage());
        } else if (command.equals("/start@BarCockBot")) {
            LoggingUtil.logSentCommand(update.getMessage());
            return registrationBotCommand.getResponse(update.getMessage());
        }

        return Stream.empty();
    }

    private Stream<CommandResponse> processPrivateChatUpdate(Update update) {
        String command = update.getMessage().getText();
        String lowerCaseCommand = command.toLowerCase();

        if (lowerCaseCommand.startsWith("барбот")) {
            LoggingUtil.logSentCommand(update.getMessage());
            return barBotCommandsFactory.processBotCommand(lowerCaseCommand, update.getMessage());
        } else if (command.equals("/start")) {
            LoggingUtil.logSentCommand(update.getMessage());
            return registrationBotCommand.getResponse(update.getMessage());
        }

        return Stream.empty();
    }

    private void sendMessage(CommandResponse response) {
        try {
            this.execute(response.message());
            Thread.sleep(response.delay());
        } catch (TelegramApiException e) {
            log.error("Error executing message: {}, error: {}", message.getText(), e.getMessage());
        } catch (InterruptedException e) {
            log.error("Error delaying message: {}, error: {}", message.getText(), e.getMessage());
        }
    }
}
