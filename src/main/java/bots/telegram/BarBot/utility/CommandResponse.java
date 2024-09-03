package bots.telegram.BarBot.utility;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public record CommandResponse(SendMessage message,
                              int delay) {
}
