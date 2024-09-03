package bots.telegram.BarBot.utility;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class LoggingUtil {
    public static void logMessageFromBot(Message message) {
        log.info("Bot message received: {} From: {} id={} chat: {} id={} type={}",
                message.getText(),
                message.getFrom().getUserName(),
                message.getFrom().getId(),
                message.getChat().getTitle(),
                message.getChatId(),
                message.getChat().getType());
    }

    public static void logMessageNotFromChat(Message message) {
        log.info("Bot not group message received: {}. From: {} ,id={}. Chat: {}, id={}, type={}",
                message.getText(),
                message.getFrom().getUserName(),
                message.getFrom().getId(),
                message.getChat().getTitle(),
                message.getChatId(),
                message.getChat().getType());
    }

    public static void logSentCommand(Message message) {
        log.info("BarBot command message received: {} From: {} id={} chat: {} id={} type={}",
                message.getText(),
                message.getFrom().getUserName(),
                message.getFrom().getId(),
                message.getChat().getTitle(),
                message.getChatId(),
                message.getChat().getType());
    }
}
